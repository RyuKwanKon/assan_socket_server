package org.asansocketserver.domain.sensor.mongorepository;

import org.asansocketserver.domain.sensor.entity.SensorAccelerometer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface SensorAccelerometerRepository extends MongoRepository<SensorAccelerometer, String>, SensorCustomRepository {
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);
}
