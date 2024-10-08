package org.asansocketserver.domain.sensor.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.sensor.entity.sensorType.Light;

@Builder(access = AccessLevel.PRIVATE)
public record LightResponseDto(
        Integer value,
        String timestamp
) {
    public static LightResponseDto of(Light light) {
        return LightResponseDto.builder()
                .value(light.getValue())
                .timestamp(light.getTimeStamp())
                .build();
    }
}
