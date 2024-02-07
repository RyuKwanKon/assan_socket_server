package org.asansocketserver.domain.sensor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.sensor.dto.request.*;
import org.asansocketserver.domain.sensor.dto.response.*;
import org.asansocketserver.domain.sensor.entity.sensorType.*;
import org.asansocketserver.domain.sensor.mongorepository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.asansocketserver.domain.sensor.entity.sensorType.Accelerometer.createAccelerometer;
import static org.asansocketserver.domain.sensor.entity.sensorType.Barometer.createBarometer;
import static org.asansocketserver.domain.sensor.entity.sensorType.Gyroscope.createGyroscope;
import static org.asansocketserver.domain.sensor.entity.sensorType.HeartRate.createHeartRate;
import static org.asansocketserver.domain.sensor.entity.sensorType.Light.createLight;

@Slf4j
@RequiredArgsConstructor
@Service
public class SensorService {
    private final SensorRepository sensorRepository;

    public AccelerometerResponseDto sendAccelerometer(Map<String, Object> simpSessionAttributes,
                                                      AccelerometerRequestDto accelerometerRequestDto) {
        Long watchId = getWatchIdFromSession(simpSessionAttributes);
        Accelerometer createdAccelerometer = createAccelerometer(accelerometerRequestDto);
        updateAccelerometer(watchId, createdAccelerometer);
        return AccelerometerResponseDto.of(createdAccelerometer);
    }

    public BarometerResponseDto sendBarometer(Map<String, Object> simpSessionAttributes,
                                              BarometerRequestDto barometerRequestDto) {
        Long watchId = getWatchIdFromSession(simpSessionAttributes);
        Barometer barometer = createBarometer(barometerRequestDto);
        updateBarometer(watchId, barometer);
        return BarometerResponseDto.of(barometer);
    }

    public GyroscopeResponseDto sendGyroscope(Map<String, Object> simpSessionAttributes,
                                              GyroscopeRequestDto gyroscopeRequestDto) {
        Long watchId = getWatchIdFromSession(simpSessionAttributes);
        Gyroscope gyroscope = createGyroscope(gyroscopeRequestDto);
        updateGyroscope(watchId, gyroscope);
        return GyroscopeResponseDto.of(gyroscope);
    }

    public HeartRateResponseDto sendHeartRate(Map<String, Object> simpSessionAttributes,
                                              HeartRateRequestDto heartRateRequestDto) {
        Long watchId = getWatchIdFromSession(simpSessionAttributes);
        HeartRate heartRate = createHeartRate(heartRateRequestDto);
        updateHeartRate(watchId, heartRate);
        return HeartRateResponseDto.of(heartRate);
    }

    public LightResponseDto sendLight(Map<String, Object> simpSessionAttributes,
                                      LightRequestDto lightRequestDto) {
        Long watchId = getWatchIdFromSession(simpSessionAttributes);
        Light light = createLight(lightRequestDto);
        updateLight(watchId, light);
        return LightResponseDto.of(light);
    }

    private Long getWatchIdFromSession(Map<String, Object> simpSessionAttributes) {
        return (Long) simpSessionAttributes.get("watchId");
    }

    private void updateAccelerometer(Long watchId, Accelerometer accelerometer) {
        sensorRepository.updateAccelerometer(watchId, accelerometer);
    }

    private void updateBarometer(Long watchId, Barometer barometer) {
        sensorRepository.updateBarometer(watchId, barometer);
    }

    private void updateGyroscope(Long watchId, Gyroscope gyroscope) {
        sensorRepository.updateGyroscope(watchId, gyroscope);
    }

    private void updateHeartRate(Long watchId, HeartRate heartRate) {
        sensorRepository.updateHeartRate(watchId, heartRate);
    }

    private void updateLight(Long watchId, Light light) {
        sensorRepository.updateLight(watchId, light);
    }
}
