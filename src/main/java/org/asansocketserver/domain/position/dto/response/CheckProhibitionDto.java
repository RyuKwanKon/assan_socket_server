package org.asansocketserver.domain.position.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CheckProhibitionDto(
        Long watchId,
        Long imageId,
        String watchName,
        String watchHost,
        String position,
        LocalDateTime currentTime
)
{
    public static CheckProhibitionDto of(Long watchId,Long imageId , String watchName,String watchHost,String position) {
        return CheckProhibitionDto.builder()
                .watchId(watchId)
                .imageId(imageId)
                .watchName(watchName)
                .watchHost(watchHost)
                .position(position)
                .currentTime(LocalDateTime.now()).build();
    }
}
