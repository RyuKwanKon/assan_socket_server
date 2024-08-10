package org.asansocketserver.domain.watch.dto.web.response;

import lombok.Builder;

import java.util.List;

@Builder
public record WatchProhibitedCoordinatesUpdateResponseDto(
        Long watchId,
        List<Long> prohibitedCoordinatesIds) {

    public static WatchProhibitedCoordinatesUpdateResponseDto of(Long watchId, List<Long> prohibitedCoordinatesIds){
        return WatchProhibitedCoordinatesUpdateResponseDto.builder()
                .watchId(watchId)
                .prohibitedCoordinatesIds(prohibitedCoordinatesIds).build();
    }
}
