package org.asansocketserver.domain.sensor.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.asansocketserver.domain.sensor.dto.request.HeartRateRequestDto;
import org.asansocketserver.domain.sensor.entity.sensorType.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Document(collection = "sensor_heart_rate")
public class SensorHeartRate {
    @Id
    @Field(name = "_id")
    private String id;
    @Field(name = "date")
    private LocalDate date;
    @Field(name = "watch_id")
    private Long watchId;
    @Field(name = "value")
    private Integer value;
    @Field(name = "timeStamp")
    private Long timeStamp;

    public static SensorHeartRate createSensor(Long watchId, HeartRateRequestDto heartRateRequestDto) {
        return SensorHeartRate.builder()
                .date(LocalDate.now())
                .watchId(watchId)
                .value(heartRateRequestDto.value())
                .timeStamp(heartRateRequestDto.timeStamp())
                .build();
    }
}
