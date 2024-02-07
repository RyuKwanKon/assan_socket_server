package org.asansocketserver.domain.sensor.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.sensor.entity.sensorType.Accelerometer;

@Builder(access = AccessLevel.PRIVATE)
public record AccelerometerResponseDto(
        Float accX,
        Float accY,
        Float accZ,
        String timeStamp
) {
    public static AccelerometerResponseDto of(Accelerometer accelerometer) {
        return AccelerometerResponseDto.builder()
                .accX(accelerometer.getAccX())
                .accY(accelerometer.getAccY())
                .accZ(accelerometer.getAccZ())
                .timeStamp(accelerometer.getTimeStamp())
                .build();
    }
}
