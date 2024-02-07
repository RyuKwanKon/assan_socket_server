package org.asansocketserver.domain.sensor.entity.sensorType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.asansocketserver.domain.sensor.dto.request.GyroscopeRequestDto;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Gyroscope {
    private Float gyroX;
    private Float gyroY;
    private Float gyroZ;
    private String timeStamp;

    public static Gyroscope createGyroscope(GyroscopeRequestDto requestDto) {
        return Gyroscope.builder()
                .gyroX(requestDto.gyroX())
                .gyroY(requestDto.gyroY())
                .gyroZ(requestDto.gyroZ())
                .timeStamp(requestDto.timeStamp())
                .build();
    }
}
