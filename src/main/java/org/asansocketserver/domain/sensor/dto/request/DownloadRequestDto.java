package org.asansocketserver.domain.sensor.dto.request;

import java.time.LocalDate;
import java.util.List;

public record DownloadRequestDto(
        List<Integer> patientId, // 환자 ID 목록
        List<Integer> sensorId,  // 센서 ID 목록
        List<String> patientName, // 환자 이름 목록
        LocalDate startDate,      // 시작 날짜
        LocalDate lastDate        // 종료 날짜
) {}
