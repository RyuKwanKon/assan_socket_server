package org.asansocketserver.domain.position.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CheckContactionDto(
        Long watchId,
        Long imageId,
        String watchName,
        String watchHost,
        String contactedWatchName,
        String position,
        LocalDateTime currentTime

) {
    public static CheckContactionDto of(Long watchId,Long imageId, String watchName,String watchHost,String contactedWatchName,String position) {
        return CheckContactionDto.builder()
                .watchId(watchId)
                .imageId(imageId)
                .watchName(watchName)
                .watchHost(watchHost)
                .contactedWatchName(contactedWatchName)
                .position(position)
                .currentTime(LocalDateTime.now()).build();
    }
}
