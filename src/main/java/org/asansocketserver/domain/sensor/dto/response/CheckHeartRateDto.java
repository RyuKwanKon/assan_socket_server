package org.asansocketserver.domain.sensor.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CheckHeartRateDto(
        Long watchId,
        String watchName,
        String watchHost,
        Integer heartRate,
        Long imageId,
        String position,
        String color,
        LocalDateTime currentTime

) {
    public static CheckHeartRateDto of(Long watchId, String watchName, String watchHost,Long imageId,String position,String color ,Integer heartRate) {
        return CheckHeartRateDto.builder()
                .watchId(watchId)
                .watchName(watchName)
                .watchHost(watchHost)
                .heartRate(heartRate)
                .imageId(imageId)
                .position(position)
                .color(color)
                .currentTime(LocalDateTime.now()).build();
    }
}

