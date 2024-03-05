package org.asansocketserver.domain.sensor.mongorepository.accelerometer;

import org.asansocketserver.domain.sensor.entity.SensorAccelerometer;
import org.asansocketserver.domain.sensor.mongorepository.SensorCustomRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface SensorAccelerometerRepository extends MongoRepository<SensorAccelerometer, String>, SensorAccelerometerCustomRepository {
    List<SensorAccelerometer> findAllByWatchIdAndDate(Long watchId, LocalDate date);
}
