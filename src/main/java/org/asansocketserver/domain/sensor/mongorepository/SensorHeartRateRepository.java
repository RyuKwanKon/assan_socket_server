package org.asansocketserver.domain.sensor.mongorepository;

import org.asansocketserver.domain.sensor.entity.SensorHeartRate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SensorHeartRateRepository extends MongoRepository<SensorHeartRate, String>, SensorCustomRepository {
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);

    Optional<SensorHeartRate> findOneByWatchIdAndDate(Long watchId, LocalDate date);
}
