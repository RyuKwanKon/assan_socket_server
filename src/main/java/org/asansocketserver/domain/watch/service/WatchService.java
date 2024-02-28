package org.asansocketserver.domain.watch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.sensor.entity.*;
import org.asansocketserver.domain.sensor.mongorepository.*;
import org.asansocketserver.domain.watch.dto.request.WatchRequestDto;
import org.asansocketserver.domain.watch.dto.request.WatchUpdateRequestDto;
import org.asansocketserver.domain.watch.dto.response.WatchAllResponseDto;
import org.asansocketserver.domain.watch.dto.response.WatchResponseDto;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.asansocketserver.global.error.exception.ConflictException;
import org.asansocketserver.global.error.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.asansocketserver.global.error.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class WatchService {
    private final WatchRepository watchRepository;
    private final SensorAccelerometerRepository sensorAccelerometerRepository;
    private final SensorBarometerRepository sensorBarometerRepository;
    private final SensorGyroscopeRepository sensorGyroscopeRepository;
    private final SensorHeartRateRepository sensorHeartRateRepository;
    private final SensorLightRepository sensorLightRepository;

    public WatchResponseDto updateWatchInfo(Long watchId, WatchUpdateRequestDto watchUpdateRequestDto) {
        Watch watch = findByWatchIdOrThrow(watchId);
        watch.updateWatch(watchUpdateRequestDto);
        return WatchResponseDto.of(watch);
    }

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
        createAccelerometerAndSave(createdWatch.getId());
        createBarometerAndSave(createdWatch.getId());
        createGyroscopeAndSave(createdWatch.getId());
        createHeartRateAndSave(createdWatch.getId());
        createLightAndSave(createdWatch.getId());
        return WatchResponseDto.of(createdWatch);
    }

    private void createAccelerometerAndSave(Long watchId) {
        SensorAccelerometer sensorAccelerometer = SensorAccelerometer.createSensor(watchId);
        sensorAccelerometerRepository.save(sensorAccelerometer);
    }

    private void createBarometerAndSave(Long watchId) {
        SensorBarometer sensorBarometer = SensorBarometer.createSensor(watchId);
        sensorBarometerRepository.save(sensorBarometer);
    }

    private void createGyroscopeAndSave(Long watchId) {
        SensorGyroscope sensorGyroscope = SensorGyroscope.createSensor(watchId);
        sensorGyroscopeRepository.save(sensorGyroscope);
    }

    private void createHeartRateAndSave(Long watchId) {
        SensorHeartRate sensorHeartRate = SensorHeartRate.createSensor(watchId);
        sensorHeartRateRepository.save(sensorHeartRate);
    }

    private void createLightAndSave(Long watchId) {
        SensorLight sensorLight = SensorLight.createSensor(watchId);
        sensorLightRepository.save(sensorLight);
    }

    private Watch createWatchAndSave(WatchRequestDto watchRequestDto) {
        Watch createdWatch = Watch.createWatch(watchRequestDto.uuid(), watchRequestDto.device());
        return watchRepository.save(createdWatch);
    }

    private void validateDuplicateWatch(WatchRequestDto watchRequestDto) {
        if (watchRepository.existsByUuid(watchRequestDto.uuid()))
            throw new ConflictException(DUPLICATE_WATCH_UUID);
    }

    private Watch findByWatchIdOrThrow(Long id) {
        return watchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(WATCH_NOT_FOUND));
    }

    private Watch findByWatchOrThrow(String uuid) {
        return watchRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(WATCH_UUID_NOT_FOUND));
    }

    private List<Watch> findAllByWatch() {
        return watchRepository.findAll();
    }
}
