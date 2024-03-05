package org.asansocketserver.domain.sensor.mongorepository.Gyroscope;

import org.asansocketserver.domain.sensor.entity.SensorGyroscope;

import java.util.List;

public interface SensorGyroscopeCustomRepository {
    void deleteAllGyroscopes(List<String> sensorGyroscopeIdList);
}
