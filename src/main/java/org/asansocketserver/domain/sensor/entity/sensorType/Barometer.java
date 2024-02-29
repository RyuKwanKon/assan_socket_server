package org.asansocketserver.domain.sensor.entity.sensorType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.asansocketserver.domain.sensor.dto.request.BarometerRequestDto;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Barometer {
    private Float value;
    private String timeStamp;

    public static Barometer createBarometer(BarometerRequestDto requestDto) {
        return Barometer.builder()
                .value(requestDto.value())
                .timeStamp(requestDto.timeStamp().toString())
                .build();
    }
}
