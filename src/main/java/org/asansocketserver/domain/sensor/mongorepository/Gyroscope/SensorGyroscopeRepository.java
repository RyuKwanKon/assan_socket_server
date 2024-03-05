package org.asansocketserver.domain.sensor.mongorepository.Gyroscope;

import org.asansocketserver.domain.sensor.entity.SensorGyroscope;
import org.asansocketserver.domain.sensor.mongorepository.SensorCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface SensorGyroscopeRepository extends MongoRepository<SensorGyroscope, String>, SensorGyroscopeCustomRepository {
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);

    List<SensorGyroscope> findAllByWatchIdAndDate(Long watchId, LocalDate date);
}
