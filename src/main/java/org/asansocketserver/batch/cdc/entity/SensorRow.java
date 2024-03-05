package org.asansocketserver.batch.cdc.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.asansocketserver.domain.sensor.entity.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class SensorRow {
    private String timeStamp;
    private Float accX;
    private Float accY;
    private Float accZ;
    private Float barometerValue;
    private Float gyroX;
    private Float gyroY;
    private Float gyroZ;
    private Integer heartRateValue;
    private Integer lightValue;

    public static SensorRow of(String timeStamp, Float accX, Float accY, Float accZ,
                               Float barometerValue, Float gyroX, Float gyroY, Float gyroZ,
                               Integer heartRateValue, Integer lightValue) {
        return SensorRow.builder()
                .timeStamp(timeStamp)
                .accX(accX)
                .accY(accY)
                .accZ(accZ)
                .barometerValue(barometerValue)
                .gyroX(gyroX)
                .gyroY(gyroY)
                .gyroZ(gyroZ)
                .heartRateValue(heartRateValue)
                .lightValue(lightValue)
                .build();
    }

    public static SensorRow accelerometerOf(SensorAccelerometer sensorAccelerometer) {
        return SensorRow.builder()
                .timeStamp(sensorAccelerometer.getTimestamp().toString())
                .accX(sensorAccelerometer.getAccX())
                .accY(sensorAccelerometer.getAccY())
                .accZ(sensorAccelerometer.getAccZ())
                .build();
    }

    public static SensorRow barometerOf(SensorBarometer sensorBarometer) {
        return SensorRow.builder()
                .timeStamp(sensorBarometer.getTimestamp().toString())
                .barometerValue(sensorBarometer.getValue())
                .build();
    }

    public static SensorRow gyroscopeOf(SensorGyroscope sensorGyroscope) {
        return SensorRow.builder()
                .timeStamp(sensorGyroscope.getTimeStamp().toString())
                .gyroX(sensorGyroscope.getGyroX())
                .gyroY(sensorGyroscope.getGyroY())
                .gyroZ(sensorGyroscope.getGyroZ())
                .build();
    }

    public static SensorRow heartRateOf(SensorHeartRate sensorHeartRate) {
        return SensorRow.builder()
                .timeStamp(sensorHeartRate.getTimeStamp().toString())
                .heartRateValue(sensorHeartRate.getValue())
                .build();
    }

    public static SensorRow lightOf(SensorLight sensorLight) {
        return SensorRow.builder()
                .timeStamp(sensorLight.getTimeStamp().toString())
                .lightValue(sensorLight.getValue())
                .build();
    }
}
