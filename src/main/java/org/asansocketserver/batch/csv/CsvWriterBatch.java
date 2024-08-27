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
    @Scheduled(cron = "0 32 15 * * *")
    public void createCsvTask() {
        LocalDate now = LocalDate.now();
        List<Watch> watchList = findAllByWatch();
        watchList.forEach(watch -> {
            String fileName = "ID_" + watch.getId() + "_data_" + now + ".csv";
            createAndWriteSensorDataAtCsv(watch, fileName, now);
        });
    }

    private void createAndWriteSensorDataAtCsv(Watch watch, String baseFileName, LocalDate now) {
        SensorData sensorData = findSensorDataByWatchIdAndLocalDate(watch, now);
        int maxRowsPerFile = 100000;
        int fileIndex = 0;
        int currentRow = 0; // 현재 파일에 작성된 행 수

        FileWriter writer = null;

        try {
            if(sensorData != null) {
                for (SensorRow sensorRow : sensorData.getSensorRowList()) {
                    // 새로운 파일이 필요하면 파일을 닫고 새 파일을 엽니다.
                    if (currentRow % maxRowsPerFile == 0) {
                        if (writer != null) {
                            writer.close();
                        }
//                        String fileName = "/home/bin/" + baseFileName + "_" + (fileIndex++) + ".csv";
                        String fileName = "/Users/parkjaeseok/Desktop/" + baseFileName + "_" + (fileIndex++) + ".csv";
                        writer = new FileWriter(fileName);
                        writer.write("timestamp,accX,accY,accZ,barometer,gyroX,gyroY,gyroZ,heartRate,light\n");
                    }
                    writeDataRow(writer, sensorRow);
                    currentRow++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void writeDataRow(FileWriter writer, SensorRow sensorRow) throws IOException {
        String rowDate = sensorRow.getTimestamp() + "," +
                sensorRow.getAccX() + "," +
                sensorRow.getAccY() + "," +
                sensorRow.getAccZ() + "," +
                sensorRow.getBarometerValue() + "," +
                sensorRow.getGyroX() + "," +
                sensorRow.getGyroY() + "," +
                sensorRow.getGyroZ() + "," +
                sensorRow.getHeartRateValue() + "," +
                sensorRow.getLightValue() ;
        writer.write(rowDate + "\n");
    }


    private SensorData findSensorDataByWatchIdAndLocalDate(Watch watch, LocalDate localDate) {
        return sensorDataRepository.findByWatchIdAndDate(watch.getId(), localDate);
    }

    private List<Watch> findAllByWatch() {
        return watchRepository.findAll();
    }
}
