package org.asansocketserver.domain.sensor.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.asansocketserver.domain.sensor.entity.sensorType.Barometer;
import org.asansocketserver.domain.sensor.entity.sensorType.Gyroscope;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Document(collection = "sensor_gyroscope")
public class SensorGyroscope {
    @Id
    @Field(name = "_id")
    private String id;
    @Field(name = "date")
    private LocalDate date;
    @Field(name = "watch_id")
    private Long watchId;
    @Builder.Default
    List<Gyroscope> gyroscopeList = new ArrayList<>();

    public static SensorGyroscope createSensor(Long watchId) {
        return SensorGyroscope.builder()
                .date(LocalDate.now())
                .watchId(watchId)
                .build();
    }
}
