package org.asansocketserver.domain.sensor.entity.sensorType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.asansocketserver.domain.sensor.dto.request.HeartRateRequestDto;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class HeartRate {
    private Integer value;
    private String timeStamp;

    public static HeartRate createHeartRate(HeartRateRequestDto requestDto) {
        return HeartRate.builder()
                .value(requestDto.value())
                .timeStamp(requestDto.timeStamp())
                .build();
    }
}
