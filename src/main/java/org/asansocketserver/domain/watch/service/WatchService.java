package org.asansocketserver.domain.watch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.sensor.entity.Sensor;
import org.asansocketserver.domain.sensor.mongorepository.SensorRepository;
import org.asansocketserver.domain.watch.dto.request.WatchRequestDto;
import org.asansocketserver.domain.watch.dto.response.WatchAllResponseDto;
import org.asansocketserver.domain.watch.dto.response.WatchResponseDto;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.asansocketserver.global.error.exception.ConflictException;
import org.asansocketserver.global.error.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.asansocketserver.global.error.ErrorCode.DUPLICATE_WATCH_UUID;
import static org.asansocketserver.global.error.ErrorCode.WATCH_UUID_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class WatchService {
    private final WatchRepository watchRepository;
    private final SensorRepository sensorRepository;

    public WatchAllResponseDto findAllWatch() {
        List<Watch> watchList = findAllByWatch();
        List<WatchResponseDto> watchResponseDtoList = WatchResponseDto.listOf(watchList);
        return WatchAllResponseDto.of(watchResponseDtoList);
    }

    public WatchResponseDto findWatch(String uuid) {
        Watch watch = findByWatchOrThrow(uuid);
        return WatchResponseDto.of(watch);
    }

    public WatchResponseDto createWatch(WatchRequestDto watchRequestDto) {
        validateDuplicateWatch(watchRequestDto);
        Watch createdWatch = createWatchAndSave(watchRequestDto);
        createSensorAndSave(createdWatch);
        return WatchResponseDto.of(createdWatch);
    }

    private void createSensorAndSave(Watch watch) {
        Sensor sensor = Sensor.createSensor(watch.getId());
        sensorRepository.save(sensor);
    }

    private Watch createWatchAndSave(WatchRequestDto watchRequestDto) {
        Watch createdWatch = Watch.createWatch(watchRequestDto.uuid());
        return watchRepository.save(createdWatch);
    }

    private void validateDuplicateWatch(WatchRequestDto watchRequestDto) {
        if (watchRepository.existsByUuid(watchRequestDto.uuid()))
            throw new ConflictException(DUPLICATE_WATCH_UUID);
    }

    private Watch findByWatchOrThrow(String uuid) {
        return watchRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(WATCH_UUID_NOT_FOUND));
    }

    private List<Watch> findAllByWatch() {
        return watchRepository.findAll();
    }
}
