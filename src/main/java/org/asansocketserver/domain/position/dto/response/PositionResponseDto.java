package org.asansocketserver.domain.position.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record PositionResponseDto(
        Long watchId,
        LocalDateTime currentTime,
        String position
) {
    public static PositionResponseDto of(Long watchId, String date) {
        return PositionResponseDto.builder()
                .watchId(watchId)
                .currentTime(LocalDateTime.now())
                .position(date)
                .build();
    }
}
