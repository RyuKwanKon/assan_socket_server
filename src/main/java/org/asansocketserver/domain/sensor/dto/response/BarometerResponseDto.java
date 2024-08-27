package org.asansocketserver.domain.sensor.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.sensor.entity.sensorType.Barometer;

@Builder(access = AccessLevel.PRIVATE)
public record BarometerResponseDto(
        Float value,
        String timestamp
) {
    public static BarometerResponseDto of(Barometer barometer) {
        return BarometerResponseDto.builder()
                .value(barometer.getValue())
                .timestamp(barometer.getTimestamp())
                .build();
    }
}
