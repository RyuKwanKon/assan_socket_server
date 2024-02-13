package org.asansocketserver.domain.position.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record PositionResponseDto(
        LocalDateTime currentTime,
        String position
) {
    public static PositionResponseDto of(String date) {
        return PositionResponseDto.builder()
                .currentTime(LocalDateTime.now())
                .position(date)
                .build();
    }
}
