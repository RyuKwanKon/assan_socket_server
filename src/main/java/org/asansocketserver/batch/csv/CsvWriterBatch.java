package org.asansocketserver.batch.csv;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.batch.cdc.entity.SensorData;
import org.asansocketserver.batch.cdc.entity.SensorRow;
import org.asansocketserver.batch.cdc.repository.SensorDataRepository;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
@Component
public class CsvWriterBatch {
    private final WatchRepository watchRepository;
    private final SensorDataRepository sensorDataRepository;

    @Transactional
    @Scheduled(cron = "0 0 23 * * *")
    public void createCsvTask() {
        LocalDate now = LocalDate.now();
        List<Watch> watchList = findAllByWatch();
        watchList.forEach(watch -> {
            String fileName = "ID_" + watch.getId() + "_data_" + now + ".csv";
            createAndWriteSensorDataAtCsv(watch, fileName, now);
        });
    }

    private void createAndWriteSensorDataAtCsv(Watch watch, String fileName, LocalDate now) {
        SensorData sensorData = findSensorDataByWatchIdAndLocalDate(watch, now);
        try (FileWriter writer = new FileWriter("/bin/home/" + fileName)) {
            writer.write("timestamp,accX,accY,accZ,barometer,gyroX,gyroY,gyroZ,heartRate,light,position\n");
            for (SensorRow sensorRow : sensorData.getSensorRowList())
                writeDataRow(writer, sensorRow);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDataRow(FileWriter writer, SensorRow sensorRow) throws IOException {
        String rowDate = sensorRow.getTimeStamp() + "," + sensorRow.getAccX() + "," + sensorRow.getAccY() + sensorRow.getAccZ() + ","
                + sensorRow.getBarometerValue() + "," + sensorRow.getGyroX() + "," + sensorRow.getGyroY() + sensorRow.getGyroZ() + ","
                + sensorRow.getHeartRateValue() + "," + sensorRow.getLightValue() + "," + "position";
        writer.write(rowDate);
    }

    private SensorData findSensorDataByWatchIdAndLocalDate(Watch watch, LocalDate localDate) {
        return sensorDataRepository.findByWatchIdAndDate(watch.getId(), localDate);
    }

    private List<Watch> findAllByWatch() {
        return watchRepository.findAll();
    }
}
