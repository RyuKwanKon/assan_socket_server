package org.asansocketserver.batch.cdc.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Document(collection = "sensor_data")
public class SensorData {
    @Id
    @Field(name = "_id")
    private String id;
    @Field(name = "date")
    private LocalDate date;
    @Field(name = "watch_id")
    private Long watchId;
    @Builder.Default
    List<SensorRow> sensorRowList = new ArrayList<>();

    public static SensorData createSensorData(Long watchId) {
        return SensorData.builder()
                .date(LocalDate.now())
                .watchId(watchId)
                .build();
    }
}
