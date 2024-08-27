package org.asansocketserver.batch.cdc.repository;

import org.asansocketserver.batch.cdc.entity.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface SensorDataRepository extends MongoRepository<SensorData, String>, SensorDataRepositoryCustom {
    boolean existsByWatchIdAndDate(Long watchId, LocalDate date);
    SensorData findByWatchIdAndDate(Long watchId, LocalDate date);

    List<SensorData> findAllByWatchIdAndDateBetween(int patientId, LocalDate localDate, LocalDate localDate1);
}
