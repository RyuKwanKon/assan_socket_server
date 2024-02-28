package org.asansocketserver.domain.sensor.utils;

import org.asansocketserver.domain.sensor.entity.*;
import org.asansocketserver.domain.sensor.entity.sensorType.*;

import java.util.List;
import java.util.Objects;

public class SensorExtractValue {
    public static Accelerometer getAccFromSensorAccelerometerAndTime(SensorAccelerometer sensorAccelerometer, String currentTime) {
        if (Objects.isNull(sensorAccelerometer))
            return null;
        List<Accelerometer> sensor = sensorAccelerometer.getAccelerometerList();
        return sensor.stream()
                .filter(s -> currentTime.equals(s.getTimeStamp()))
                .findAny()
                .orElse(null);
    }

    public static Barometer getBarFromSensorBarometerAndTime(SensorBarometer sensorBarometer, String currentTime) {
        if (Objects.isNull(sensorBarometer))
            return null;
        List<Barometer> sensor = sensorBarometer.getBarometerList();
        return sensor.stream()
                .filter(s -> currentTime.equals(s.getTimeStamp()))
                .findAny()
                .orElse(null);
    }

    public static Gyroscope getGyroFromSensorGyroscopeAndTime(SensorGyroscope sensorGyroscope, String currentTime) {
        if (Objects.isNull(sensorGyroscope))
            return null;
        List<Gyroscope> sensor = sensorGyroscope.getGyroscopeList();
        return sensor.stream()
                .filter(s -> currentTime.equals(s.getTimeStamp()))
                .findAny()
                .orElse(null);
    }

    public static HeartRate getHRFromSensorHeartRateAndTime(SensorHeartRate sensorHeartRate, String currentTime) {
        if (Objects.isNull(sensorHeartRate))
            return null;
        List<HeartRate> sensor = sensorHeartRate.getHeartRateList();
        return sensor.stream()
                .filter(s -> currentTime.equals(s.getTimeStamp()))
                .findAny()
                .orElse(null);
    }

    public static Light getLightFromSensorLightAndTime(SensorLight sensorLight, String currentTime) {
        if (Objects.isNull(sensorLight))
            return null;
        List<Light> sensor = sensorLight.getLightList();
        return sensor.stream()
                .filter(s -> currentTime.equals(s.getTimeStamp()))
                .findAny()
                .orElse(null);
    }
}
