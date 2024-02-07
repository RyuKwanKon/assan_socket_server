package org.asansocketserver.domain.sensor.dto.request;

public record BarometerRequestDto(
        Float value,
        String timeStamp
) {
}
