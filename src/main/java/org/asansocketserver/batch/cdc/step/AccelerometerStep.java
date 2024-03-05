package org.asansocketserver.batch.cdc.step;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.batch.cdc.entity.SensorRow;
import org.asansocketserver.batch.cdc.repository.SensorDataRepository;
import org.asansocketserver.domain.sensor.entity.SensorAccelerometer;
import org.asansocketserver.domain.sensor.mongorepository.accelerometer.SensorAccelerometerRepository;
import org.asansocketserver.domain.watch.entity.Watch;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AccelerometerStep {
    private final SensorAccelerometerRepository sensorAccelerometerRepository;
    private final SensorDataRepository sensorDataRepository;

    public void accelerometerStep(Watch watch) {
        List<SensorAccelerometer> sensorAccelerometerList = accelerometerReadTask(watch);
        List<SensorRow> sensorRowList = accelerometerProcessorTask(sensorAccelerometerList);
        accelerometerWriteTask(sensorRowList, watch);
    }

    private List<SensorAccelerometer> accelerometerReadTask(Watch watch) {
        return sensorAccelerometerRepository.findAllByWatchIdAndDate(watch.getId(), LocalDate.now());
    }

    private List<SensorRow> accelerometerProcessorTask(List<SensorAccelerometer> sensorAccelerometerList) {
        List<SensorRow> sensorRowList = createSensorRowList(sensorAccelerometerList);
        deleteAllAccelerometerData(sensorAccelerometerList);
        return sensorRowList;
    }

    private void accelerometerWriteTask(List<SensorRow> sensorRowList, Watch watch) {
        sensorRowList.forEach(sensorRow ->
                sensorDataRepository.updateAccelerometerData(watch, sensorRow)
        );
    }

    private List<SensorRow> createSensorRowList(List<SensorAccelerometer> sensorAccelerometerList) {
        return sensorAccelerometerList.stream()
                .map(SensorRow::accelerometerOf)
                .collect(Collectors.toList());
    }

    private void deleteAllAccelerometerData(List<SensorAccelerometer> sensorAccelerometerList) {
        List<String> sensorAccelerometers = sensorAccelerometerList.stream()
                .map(SensorAccelerometer::getId)
                .collect(Collectors.toList());
        sensorAccelerometerRepository.deleteAllAccelerometers(sensorAccelerometers);
    }
}
