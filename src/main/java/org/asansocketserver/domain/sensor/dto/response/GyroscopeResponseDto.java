package org.asansocketserver.domain.sensor.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.sensor.entity.sensorType.Gyroscope;

@Builder(access = AccessLevel.PRIVATE)
public record GyroscopeResponseDto(
        Float gyroX,
        Float gyroY,
        Float gyroZ,
        String timeStamp
) {
    public static GyroscopeResponseDto of(Gyroscope gyroscope) {
        return GyroscopeResponseDto.builder()
                .gyroX(gyroscope.getGyroX())
                .gyroY(gyroscope.getGyroY())
                .gyroZ(gyroscope.getGyroZ())
                .timeStamp(gyroscope.getTimeStamp())
                .build();
    }
}
