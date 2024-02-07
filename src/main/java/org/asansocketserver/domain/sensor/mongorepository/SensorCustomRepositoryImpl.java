package org.asansocketserver.domain.sensor.mongorepository;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.sensor.entity.Sensor;
import org.asansocketserver.domain.sensor.entity.sensorType.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.Objects;

@RequiredArgsConstructor
public class SensorCustomRepositoryImpl implements SensorCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void updateAccelerometer(final Long watchId, final Accelerometer accelerometer) {
        Sensor sensor = findSensorByWatchId(watchId);
        if (Objects.isNull(sensor)) return;
        sensor.getAccelerometerList().add(accelerometer);
        mongoTemplate.save(sensor);
    }

    @Override
    public void updateBarometer(final Long watchId, final Barometer barometer) {
        Sensor sensor = findSensorByWatchId(watchId);
        if (Objects.isNull(sensor)) return;
        sensor.getBarometerList().add(barometer);
        mongoTemplate.save(sensor);
    }

    @Override
    public void updateGyroscope(final Long watchId, final Gyroscope gyroscope) {
        Sensor sensor = findSensorByWatchId(watchId);
        if (Objects.isNull(sensor)) return;
        sensor.getGyroscopeList().add(gyroscope);
        mongoTemplate.save(sensor);
    }

    @Override
    public void updateHeartRate(final Long watchId, final HeartRate heartRate) {
        Sensor sensor = findSensorByWatchId(watchId);
        if (Objects.isNull(sensor)) return;
        sensor.getHeartRateList().add(heartRate);
        mongoTemplate.save(sensor);
    }

    @Override
    public void updateLight(final Long watchId, final Light light) {
        Sensor sensor = findSensorByWatchId(watchId);
        if (Objects.isNull(sensor)) return;
        sensor.getLightList().add(light);
        mongoTemplate.save(sensor);
    }

    private Sensor findSensorByWatchId(Long watchId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("date").is(LocalDate.now())
                .and("watchId").is(watchId));
        return mongoTemplate.findOne(query, Sensor.class, "sensor");
    }
}
