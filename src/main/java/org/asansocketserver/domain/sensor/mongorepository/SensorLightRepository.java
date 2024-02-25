package org.asansocketserver.domain.sensor.mongorepository;

import org.asansocketserver.domain.sensor.entity.SensorLight;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface SensorLightRepository extends MongoRepository<SensorLight, String>, SensorCustomRepository {
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);
}
