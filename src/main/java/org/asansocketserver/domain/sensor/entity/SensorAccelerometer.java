package org.asansocketserver.domain.sensor.entity;

import jakarta.persistence.Id;
import lombok.*;
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
@Document(collection = "sensor_accelerometer")
public class SensorAccelerometer {
    @Id
    @Field(name = "_id")
    private String id;
    @Field(name = "date")
    private LocalDate date;
    @Field(name = "watch_id")
    private Long watchId;
    @Field(name = "accelerometer_list")
    @Builder.Default
    List<Accelerometer> accelerometerList = new ArrayList<>();

    public static SensorAccelerometer createSensor(Long watchId) {
        return SensorAccelerometer.builder()
                .date(LocalDate.now())
                .watchId(watchId)
                .build();
    }

    public void addSensorDate(Accelerometer accelerometer) {
        this.accelerometerList.add(accelerometer);
    }
}
