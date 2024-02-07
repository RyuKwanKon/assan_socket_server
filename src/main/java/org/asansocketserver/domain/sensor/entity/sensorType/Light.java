package org.asansocketserver.domain.sensor.entity.sensorType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.asansocketserver.domain.sensor.dto.request.LightRequestDto;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Light {
    private Integer value;
    private String timeStamp;

    public static Light createLight(LightRequestDto requestDto) {
        return Light.builder()
                .value(requestDto.value())
                .timeStamp(requestDto.timeStamp())
                .build();
    }
}
