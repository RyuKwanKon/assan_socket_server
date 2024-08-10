package org.asansocketserver.domain.watch.dto.web.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WatchIdAndNameDto(
        Long watchId,
        String watchName

) {
    public static WatchIdAndNameDto of( Long watchId, String watchName) {
        return WatchIdAndNameDto.builder()
                .watchId(watchId)
                .watchName(watchName)
                .build();
    }
}