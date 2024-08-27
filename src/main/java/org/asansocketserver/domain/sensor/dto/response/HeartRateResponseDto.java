package org.asansocketserver.domain.sensor.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.sensor.entity.sensorType.HeartRate;

@Builder(access = AccessLevel.PRIVATE)
public record HeartRateResponseDto(
        int value,
        String timeStamp
) {
    public static HeartRateResponseDto of(HeartRate heartRate) {
        return HeartRateResponseDto.builder()
                .value(heartRate.getValue())
                .timeStamp(heartRate.getTimestamp())
                .build();
    }
}
