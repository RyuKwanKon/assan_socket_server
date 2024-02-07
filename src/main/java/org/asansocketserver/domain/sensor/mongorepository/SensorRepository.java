package org.asansocketserver.domain.sensor.mongorepository;

import org.asansocketserver.domain.sensor.entity.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface SensorRepository extends MongoRepository<Sensor, String>, SensorCustomRepository {
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);
}
