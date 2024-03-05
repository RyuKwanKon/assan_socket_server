//package org.asansocketserver.domain.sensor.scheduler;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.asansocketserver.domain.position.entity.Position;
//import org.asansocketserver.domain.position.entity.PositionData;
//import org.asansocketserver.domain.position.mongorepository.PositionMongoRepository;
//import org.asansocketserver.domain.sensor.entity.*;
//import org.asansocketserver.domain.sensor.entity.sensorType.*;
//import org.asansocketserver.domain.sensor.mongorepository.*;
//import org.asansocketserver.domain.sensor.mongorepository.accelerometer.SensorAccelerometerRepository;
//import org.asansocketserver.domain.watch.dto.response.WatchLiveResponseDto;
//import org.asansocketserver.domain.watch.entity.Watch;
//import org.asansocketserver.domain.watch.entity.WatchLive;
//import org.asansocketserver.domain.watch.repository.WatchLiveRepository;
//import org.asansocketserver.domain.watch.repository.WatchRepository;
//import org.asansocketserver.socket.dto.MessageType;
//import org.asansocketserver.socket.dto.SocketBaseResponse;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Objects;
//
//import static org.asansocketserver.domain.position.util.PositionExtractValue.getPositionDateFromPositionAndTime;
//import static org.asansocketserver.domain.sensor.utils.SensorExtractValue.*;
//
//@Slf4j
//@RequiredArgsConstructor
//@EnableAsync
//@EnableScheduling
//@Component
//public class SensorScheduler {
//    private final WatchRepository watchRepository;
//    private final SensorAccelerometerRepository sensorAccelerometerRepository;
//    private final SensorBarometerRepository sensorBarometerRepository;
//    private final SensorGyroscopeRepository sensorGyroscopeRepository;
//    private final SensorHeartRateRepository sensorHeartRateRepository;
//    private final SensorLightRepository sensorLightRepository;
//    private final WatchLiveRepository watchLiveRepository;
//    private final PositionMongoRepository positionMongoRepository;
//    private final SimpMessageSendingOperations sendingOperations;
//
//    @Transactional
//    @Scheduled(cron = "0 0 0 * * *")
//    public void createCsvScheduleTask() {
//        LocalDate now = LocalDate.now();
//        Long todayTimeStamp = createTimeStampFromLocalDateTime(LocalDateTime.now().minusDays(1));
//        Long nextDayTimeStamp = createTimeStampFromLocalDateTime(LocalDateTime.now());
//        List<Watch> watchList = findAllByWatch();
//        watchList.forEach(watch -> {
//            String fileName = "ID_" + watch.getId() + "_data_" + now.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv";
//            createAndWriteSensorDataAtCsv(watch, fileName, todayTimeStamp, nextDayTimeStamp);
//        });
//    }
//
//    @Transactional
//    @Scheduled(cron = "30 * * * * *")
//    public void broadcastWatchList() {
//        List<WatchLiveResponseDto> responseDto = findAllWatch();
//        sendingOperations.convertAndSend("/queue/sensor/9999999", SocketBaseResponse.of(MessageType.WATCH_LIST, responseDto));
//    }
//
//    private void createAndWriteSensorDataAtCsv(Watch watch, String fileName, Long todayTimeStamp, Long nextDayTimeStamp) {
//        try (FileWriter writer = new FileWriter("/path/to/host/mount/" + fileName)) {
//            writer.write("timestamp,accX,accY,accZ,barometer,gyroX,gyroY,gyroZ,heartRate,light,position\n");
//            for (int idx = 0; todayTimeStamp + idx < nextDayTimeStamp; idx++) {
//                String rowDate = getRowData(watch, String.valueOf(todayTimeStamp + idx));
//                writer.write(rowDate);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getRowData(Watch watch, String currentTime) {
//        SensorAccelerometer sensorAccelerometer = findSensorAccelerometerByWatchIdAndDateTime(watch.getId());
//        Accelerometer accelerometer = getAccFromSensorAccelerometerAndTime(sensorAccelerometer, currentTime);
//        float accX = Objects.isNull(accelerometer) ? 0: accelerometer.getAccX();
//        float accY = Objects.isNull(accelerometer) ? 0: accelerometer.getAccY();
//        float accZ = Objects.isNull(accelerometer) ? 0: accelerometer.getAccZ();
//
//        SensorBarometer sensorBarometer = findSensorBarometerByWatchIdAndDateTime(watch.getId());
//        Barometer barometer = getBarFromSensorBarometerAndTime(sensorBarometer, currentTime);
//        float barometerValue = Objects.isNull(barometer) ? 0: barometer.getValue();
//
//        SensorGyroscope sensorGyroscope = findSensorGyroscopeByWatchIdAndDateTime(watch.getId());
//        Gyroscope gyroscope = getGyroFromSensorGyroscopeAndTime(sensorGyroscope, currentTime);
//        float gyroX = Objects.isNull(gyroscope) ? 0: gyroscope.getGyroX();
//        float gyroY= Objects.isNull(gyroscope) ? 0: gyroscope.getGyroY();
//        float gyroZ= Objects.isNull(gyroscope) ? 0: gyroscope.getGyroZ();
//
//        SensorHeartRate sensorHeartRate = findSensorHeartRateByWatchIdAndDateTime(watch.getId());
//        HeartRate heartRate = getHRFromSensorHeartRateAndTime(sensorHeartRate, currentTime);
//        int heartRateValue = Objects.isNull(heartRate) ? 0: heartRate.getValue();
//
//        SensorLight sensorLight = findSensorLightByWatchIdAndDateTime(watch.getId());
//        Light light = getLightFromSensorLightAndTime(sensorLight, currentTime);
//        int lightValue = Objects.isNull(light) ? 0: light.getValue();
//
//        Position position = findPositionByWatchIdAndDateTime(watch.getId());
//        PositionData positionData = getPositionDateFromPositionAndTime(position, currentTime);
//        String stringPositionDate = Objects.isNull(positionData) ? "none": positionData.getPosition();
//
//        return currentTime + "," + accX + "," + accY + "," + accZ
//                + "," + barometerValue + "," + gyroX + "," + gyroY + "," + gyroZ
//                + "," + heartRateValue + "," + lightValue + "," + stringPositionDate;
//    }
//
//    private Long createTimeStampFromLocalDateTime(LocalDateTime localDateTime) {
//        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
//        return instant.getEpochSecond();
//    }
//
//    private List<WatchLiveResponseDto> findAllWatch() {
//        List<WatchLive> watchLiveList = findAllWatchInRedis();
//        return WatchLiveResponseDto.liveListOf(watchLiveList);
//    }
//
//
//    private void createBarometerAndSave(Long watchId) {
//        if (sensorBarometerRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
//        SensorBarometer sensorBarometer = SensorBarometer.createSensor(watchId);
//        sensorBarometerRepository.save(sensorBarometer);
//    }
//
//    private void createGyroscopeAndSave(Long watchId) {
//        if (sensorGyroscopeRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
//        SensorGyroscope sensorGyroscope = SensorGyroscope.createSensor(watchId);
//        sensorGyroscopeRepository.save(sensorGyroscope);
//    }
//
//    private void createHeartRateAndSave(Long watchId) {
//        if (sensorHeartRateRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
//        SensorHeartRate sensorHeartRate = SensorHeartRate.createSensor(watchId);
//        sensorHeartRateRepository.save(sensorHeartRate);
//    }
//
//    private void createLightAndSave(Long watchId) {
//        if (sensorLightRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
//        SensorLight sensorLight = SensorLight.createSensor(watchId);
//        sensorLightRepository.save(sensorLight);
//    }
//
//    private SensorAccelerometer findSensorAccelerometerByWatchIdAndDateTime(Long watchId) {
//        return sensorAccelerometerRepository.findOneByWatchIdAndDate(watchId, LocalDate.now());
//    }
//
//    private SensorBarometer findSensorBarometerByWatchIdAndDateTime(Long watchId) {
//        return sensorBarometerRepository.findOneByWatchIdAndDate(watchId, LocalDate.now());
//    }
//
//    private SensorGyroscope findSensorGyroscopeByWatchIdAndDateTime(Long watchId) {
//        return sensorGyroscopeRepository.findOneByWatchIdAndDate(watchId, LocalDate.now());
//    }
//
//    private SensorHeartRate findSensorHeartRateByWatchIdAndDateTime(Long watchId) {
//        return sensorHeartRateRepository.findOneByWatchIdAndDate(watchId, LocalDate.now()).get();
//    }
//
//    private SensorLight findSensorLightByWatchIdAndDateTime(Long watchId) {
//        return sensorLightRepository.findOneByWatchIdAndDate(watchId, LocalDate.now());
//    }
//
//    private Position findPositionByWatchIdAndDateTime(Long watchId) {
//        return positionMongoRepository.findOneByWatchIdAndDate(watchId, LocalDate.now());
//    }
//
//    private List<WatchLive> findAllWatchInRedis() {
//        return watchLiveRepository.findAllByLive(true);
//    }
//
//    private List<Watch> findAllByWatch() {
//        return watchRepository.findAll();
//    }
//}
