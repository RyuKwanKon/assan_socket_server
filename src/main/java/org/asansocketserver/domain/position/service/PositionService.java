package org.asansocketserver.domain.position.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.position.dto.ResultDataDTO;
import org.asansocketserver.domain.position.dto.request.BeaconDataDTO;
import org.asansocketserver.domain.position.dto.request.GetStateDTO;
import org.asansocketserver.domain.position.dto.request.PosDataDTO;
import org.asansocketserver.domain.position.dto.request.StateDTO;
import org.asansocketserver.domain.position.dto.response.PositionResponseDto;
import org.asansocketserver.domain.position.entity.BeaconData;
import org.asansocketserver.domain.position.entity.Position;
import org.asansocketserver.domain.position.entity.PositionData;
import org.asansocketserver.domain.position.entity.PositionState;
import org.asansocketserver.domain.position.mongorepository.PositionMongoRepository;
import org.asansocketserver.domain.position.repository.BeaconDataRepository;
import org.asansocketserver.domain.position.repository.PositionStateRepository;
import org.asansocketserver.domain.position.util.BeaconDataUtil;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.asansocketserver.global.error.exception.EntityNotFoundException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.asansocketserver.global.error.ErrorCode.WATCH_UUID_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PositionService {
    private final BeaconDataRepository beaconDataRepository;
    private final WatchRepository watchRepository;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final BeaconDataUtil beaconDataUtil;
    private final PositionStateRepository positionStateRepository;
    private final PositionMongoRepository positionMongoRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Object[]  countBeacon() {
        return new List[]{beaconDataRepository.findAllBeaconCount()};
    }

    public void insertState(StateDTO stateDTO) {
        Watch watch = findByWatchOrThrow(stateDTO.androidId());
        PositionState positionState =
                PositionState.createPositionState(watch.getId(), stateDTO.imageId(), stateDTO.position(), System.currentTimeMillis(),stateDTO.endTime());
        positionStateRepository.save(positionState);

        long delay = stateDTO.endTime() - System.currentTimeMillis();
        if (delay > 0) {
            scheduler.schedule(() -> {
                positionStateRepository.deleteById(watch.getId());
            }, delay, TimeUnit.MILLISECONDS);
        }
    }

    public void deleteState(StateDTO stateDTO) {
        Watch watch = findByWatchOrThrow(stateDTO.androidId());
        positionStateRepository.deleteById(watch.getId());
    }

    public Long getCollectionState(Long androidId) {
        Watch watch = findByWatchOrThrow(String.valueOf(androidId));
        PositionState positionState = positionStateRepository.findById(watch.getId()).orElse(null);
        if (positionState == null)
            return 0L;
        else
            return positionState.getEndTime();
    }


    public PositionResponseDto receiveData(PosDataDTO posData) {
        String responseDto;
        Watch watch = findByWatchOrThrow(posData.android_id());
        PositionState positionState = findByPositionStateOrNull(watch.getId());
        if (!Objects.isNull(positionState))
            responseDto = addPosData(posData, positionState.getImageId(),positionState.getPosition());
        else {
            responseDto = findPosition(posData);
            PositionData positionData = PositionData.of(responseDto);
            updatePositionData(watch.getId(), positionData);
        }
        return PositionResponseDto.of(watch.getId(),watch.getName(), responseDto);
    }

    private PositionState findByPositionStateOrNull(Long id) {
        return positionStateRepository.findById(id)
                .orElse(null);
    }

    public void deleteBeacon(String positionName) {
        List<BeaconData> beaconsByPosition = beaconDataRepository.findAllByPosition(positionName);
        beaconDataRepository.deleteAll(beaconsByPosition);
    }

    private String addPosData(PosDataDTO posData, Long imageId,String position) {
        if (posData.beaconData().isEmpty()){
            return null;
        }
        BeaconData beaconDataEntity = new BeaconData();
        beaconDataEntity.setImageId(imageId);
        beaconDataEntity.setPosition(position);
        String beaconDataJson = converBeaconDataDtoToJson(posData.beaconData());
        beaconDataEntity.setBeaconData(beaconDataJson);
        beaconDataRepository.save(beaconDataEntity);
        return null;
    }

    // 받은 PosData에서 json({uuid, rssi})을 (DB)에 저장.
    private String converBeaconDataDtoToJson(List beaconDataDTO) {
        // ObjectMapper를 사용하여 Beacon
        // DataDTO를 JSON 문자열로 변환
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(beaconDataDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String findPosition(PosDataDTO data) {
        List<BeaconData> dbDataList = beaconDataRepository.findAll();

        List<Future<List<ResultDataDTO>>> futureResults = new ArrayList<>();

        if (data.beaconData().isEmpty()){
            return null;
        }

        //클라이언트가 제공한 와이파이 데이터와 데이터베이스에 저장된 와이파이 데이터를 빠르게 비교하기 위해 다중 스레딩 사용.
        int threadNum = Runtime.getRuntime().availableProcessors();
        int sliceLen = (int) Math.ceil((double) dbDataList.size()) / threadNum;
        int knn = 7;

//        if(dbDataList.size() <= 250 ){
//            knn = 3;
//        }else if(dbDataList.size() <= 500 ){
//            knn = 7;
//        }else if(dbDataList.size() <= 750){
//            knn = 11;
//        }else {
//            knn = 15;
//        }
//

//        System.out.println("knn = " + knn);

        for (int i = 0; i < threadNum-1; i++) {
            int start = sliceLen * i;
            int end = Math.min(start + sliceLen, dbDataList.size());
            List<BeaconData> slicedDataList = dbDataList.subList(start, end);
            Future<List<ResultDataDTO>> future = taskExecutor.submit(() -> calPos(slicedDataList, data, 0.4));
            futureResults.add(future);
        }

        List<ResultDataDTO> results = futureResults.stream()
                .flatMap(future -> {
                    try {
                        return future.get().stream();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new IllegalStateException("Thread interrupted", e);
                    }
                }).collect(Collectors.toList());

        return calcKnn(results, knn);
    }


    // 클라이언트의 와이파이 데이터와 데이터베이스의 와이파이 데이터를 비교하여, 가장 가능성이 높은 위치 정보를 담은 리스트를 반환
    private List<ResultDataDTO> calPos(List<BeaconData> dbDataList, PosDataDTO inputData, double margin) {
        List<ResultDataDTO> resultList = new ArrayList<>();
        int largestCount = 0;
        for (BeaconData dbData : dbDataList) {
            List<BeaconDataDTO> dbBeaconDataList = beaconDataUtil.parseBeaconData(dbData.getBeaconData());
            int count = 0;
            int sum = 0;

            for (BeaconDataDTO dbBeaconData : dbBeaconDataList) {
                for (BeaconDataDTO inputBeaconData : inputData.beaconData()) {
                    if (dbBeaconData.bssid().equals(inputBeaconData.bssid())) {

                        count++;
                        sum += Math.abs(dbBeaconData.rssi() - inputBeaconData.rssi());
                        break;
                    }
                }
            }

            double avg = count > 0 ? (double) sum / count : Double.MAX_VALUE;
            double ratio = count > 0 ? avg / count : Double.MAX_VALUE;


            resultList.add(new ResultDataDTO(dbData.getId(), dbData.getPosition(), count, avg, ratio));
            largestCount = Math.max(largestCount, count);
        }


        int finalLargestCount = largestCount;

        return resultList.stream()
                .filter(data -> data.getCount() >= finalLargestCount * margin)  // * margin)
                .sorted(Comparator.comparingDouble(ResultDataDTO::getRatio))
                .collect(Collectors.toList());
    }

    // calpos 결과값을 기반으로 k개의 이웃값과 비교하여 최적값 반환.
    private String calcKnn(List<ResultDataDTO> results, int k) {
        // 결과를 ratio 오름차순으로 정렬
        List<ResultDataDTO> sortedResults = results.stream()
                .sorted(Comparator.comparingInt(ResultDataDTO::getCount).reversed()
                        .thenComparingDouble(ResultDataDTO::getRatio))
                .toList();




        // 가장 가까운 k개의 이웃을 선택
        List<ResultDataDTO> nearestNeighbors = sortedResults.stream()
                .limit(k)
                .toList();

//        nearestNeighbors.forEach(result ->
//                System.out.println("ID: " + result.getId() + ", Position: " + result.getPosition() +
//                        ", Count: " + result.getCount() + ", Avg: " + result.getAvg() +
//                        ", Ratio: " + result.getRatio()));

        // 이웃들 중에서 위치별 투표 수 계산
        Map<String, Integer> positionVotes = new HashMap<>();
        for (ResultDataDTO neighbor : nearestNeighbors) {
            String position = neighbor.getPosition();
            positionVotes.put(position, positionVotes.getOrDefault(position, 0) + 1);
//            System.out.println("positionVotes = " + positionVotes);
        }

        // 가장 많이 투표된 위치를 찾음
        String bestPosition = null;
        int maxVotes = -1;

        for (Map.Entry<String, Integer> entry : positionVotes.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                bestPosition = entry.getKey();
            }
        }

//        System.out.println("bestPosition = " + bestPosition);

        if (bestPosition != null) {
            for (ResultDataDTO result : nearestNeighbors) {
                if (result.getPosition().equals(bestPosition)) {
                    log.info("[resultHashMap]::" + result.getPosition());
                    return result.getPosition();
                }
            }
        }
        return null;
    }

    private Watch findByWatchOrThrow(String id) {
        return watchRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new EntityNotFoundException(WATCH_UUID_NOT_FOUND));
    }

    private void updatePositionData(Long watchId, PositionData position) {
        positionMongoRepository.updatePosition(watchId, position);
    }
}




