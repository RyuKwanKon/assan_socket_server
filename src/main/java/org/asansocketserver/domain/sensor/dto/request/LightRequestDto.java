package org.asansocketserver.domain.sensor.dto.request;

public record LightRequestDto(
        Integer value,
        String timeStamp
) {
}
