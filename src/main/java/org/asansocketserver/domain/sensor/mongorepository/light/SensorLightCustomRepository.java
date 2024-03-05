package org.asansocketserver.domain.sensor.mongorepository.light;

import java.util.List;

public interface SensorLightCustomRepository {
    void deleteAllLights(List<String> sensorLightIdList);
}
