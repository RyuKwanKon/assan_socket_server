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
@Document(collection = "sensor")
public class Sensor {
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
    @Field(name = "barometer_list")
    @Builder.Default
    List<Barometer> barometerList = new ArrayList<>();
    @Field(name = "gyroscope_list")
    @Builder.Default
    List<Gyroscope> gyroscopeList = new ArrayList<>();
    @Field(name = "heartRate_list")
    @Builder.Default
    List<HeartRate> heartRateList = new ArrayList<>();
    @Field(name = "light_list")
    @Builder.Default
    List<Light> lightList = new ArrayList<>();

    public static Sensor createSensor(Long watchId) {
        return Sensor.builder()
                .date(LocalDate.now())
                .watchId(watchId)
                .build();
    }
}
