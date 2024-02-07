package org.asansocketserver.domain.sensor.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.sensor.entity.Sensor;
import org.asansocketserver.domain.sensor.mongorepository.SensorRepository;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.reponsitory.WatchRepository;
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
    private final SensorRepository sensorRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void sensorScheduleTask() {
        List<Watch> watchList = findAllByWatch();
        watchList.forEach(watch -> createSensorAndSave(watch.getId()));
    }

    private void createSensorAndSave(Long watchId) {
        if (sensorRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        Sensor sensor = Sensor.createSensor(watchId);
        sensorRepository.save(sensor);
    }


    private List<Watch> findAllByWatch() {
        return watchRepository.findAll();
    }
}
