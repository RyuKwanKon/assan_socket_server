package org.asansocketserver.domain.sensor.mongorepository;

import org.asansocketserver.domain.sensor.entity.SensorBarometer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface SensorBarometerRepository extends MongoRepository<SensorBarometer, String>, SensorCustomRepository {
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);
}
