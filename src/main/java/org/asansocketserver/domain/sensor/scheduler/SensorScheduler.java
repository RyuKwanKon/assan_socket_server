package org.asansocketserver.domain.sensor.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.sensor.entity.*;
import org.asansocketserver.domain.sensor.mongorepository.*;
import org.asansocketserver.domain.watch.dto.response.WatchLiveResponseDto;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.entity.WatchLive;
import org.asansocketserver.domain.watch.repository.WatchLiveRepository;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.asansocketserver.socket.dto.MessageType;
import org.asansocketserver.socket.dto.SocketBaseResponse;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
@Component
public class SensorScheduler {
    private final WatchRepository watchRepository;
    private final SensorAccelerometerRepository sensorAccelerometerRepository;
    private final SensorBarometerRepository sensorBarometerRepository;
    private final SensorGyroscopeRepository sensorGyroscopeRepository;
    private final SensorHeartRateRepository sensorHeartRateRepository;
    private final SensorLightRepository sensorLightRepository;
    private final WatchLiveRepository watchLiveRepository;
    private final SimpMessageSendingOperations sendingOperations;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void sensorScheduleTask() {
        List<Watch> watchList = findAllByWatch();
        watchList.forEach(watch -> {
            createAccelerometerAndSave(watch.getId());
            createBarometerAndSave(watch.getId());
            createGyroscopeAndSave(watch.getId());
            createHeartRateAndSave(watch.getId());
            createLightAndSave(watch.getId());
        });
    }

    @Transactional
    @Scheduled(cron = "30 * * * * *")
    public void broadcastWatchList() {
        List<WatchLiveResponseDto> responseDto = findAllWatch();
        sendingOperations.convertAndSend("/queue/sensor/9999999", SocketBaseResponse.of(MessageType.WATCH_LIST, responseDto));
    }

    private List<WatchLiveResponseDto> findAllWatch() {
        List<WatchLive> watchLiveList = findAllWatchInRedis();
        return WatchLiveResponseDto.liveListOf(watchLiveList);
    }

    private void createAccelerometerAndSave(Long watchId) {
        if (sensorAccelerometerRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        SensorAccelerometer sensorAccelerometer = SensorAccelerometer.createSensor(watchId);
        sensorAccelerometerRepository.save(sensorAccelerometer);
    }

    private void createBarometerAndSave(Long watchId) {
        if (sensorBarometerRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        SensorBarometer sensorBarometer = SensorBarometer.createSensor(watchId);
        sensorBarometerRepository.save(sensorBarometer);
    }

    private void createGyroscopeAndSave(Long watchId) {
        if (sensorGyroscopeRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        SensorGyroscope sensorGyroscope = SensorGyroscope.createSensor(watchId);
        sensorGyroscopeRepository.save(sensorGyroscope);
    }

    private void createHeartRateAndSave(Long watchId) {
        if (sensorHeartRateRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        SensorHeartRate sensorHeartRate = SensorHeartRate.createSensor(watchId);
        sensorHeartRateRepository.save(sensorHeartRate);
    }

    private void createLightAndSave(Long watchId) {
        if (sensorLightRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        SensorLight sensorLight = SensorLight.createSensor(watchId);
        sensorLightRepository.save(sensorLight);
    }

    private List<WatchLive> findAllWatchInRedis() {
        return watchLiveRepository.findAllByLive(true);
    }

    private List<Watch> findAllByWatch() {
        return watchRepository.findAll();
    }
}
