package org.asansocketserver.domain.position.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record PositionResponseDto(
        Long watchId,
        String watchName,
        LocalDateTime currentTime,
        Long imageId,
        String color,
        String position
) {
    public static PositionResponseDto of(Long watchId,String watchName,Long imageId,String color,String date) {
        return PositionResponseDto.builder()
                .watchId(watchId)
                .watchName(watchName)
                .currentTime(LocalDateTime.now())
                .imageId(imageId)
                .color(color)
                .position(date)
                .build();
    }
}
