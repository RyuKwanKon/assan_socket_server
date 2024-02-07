package org.asansocketserver.domain.sensor.dto.request;

public record HeartRateRequestDto(
        String id,
        Integer value,
        String timeStamp
) {
}
