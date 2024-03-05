package org.asansocketserver.domain.sensor.mongorepository.heartrate;

import java.util.List;

public interface SensorHeartRateCustomRepository {
    void deleteAllHeartRates(List<String> sensorHeatRateIdList);
}
