package org.asansocketserver.domain.position.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CheckGenderDto(
        Long watchId,
        Long imageId,
        String watchName,
        String watchHost,
        String position,
        LocalDateTime currentTime
)
{
    public static CheckGenderDto of(Long watchId,Long imageId, String watchName,String watchHost, String position) {
        return CheckGenderDto.builder()
                .watchId(watchId)
                .imageId(imageId)
                .watchName(watchName)
                .watchHost(watchHost)
                .position(position)
                .currentTime(LocalDateTime.now()).build();
    }
}
