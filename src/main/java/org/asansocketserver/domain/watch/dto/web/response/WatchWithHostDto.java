package org.asansocketserver.domain.watch.dto.web.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.image.dto.CoordinateIDAndPositionDTO;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record WatchWithHostDto(
        String host,
        List<WatchIdAndNameDto> watchList

) {
    public static WatchWithHostDto of(String host, List<WatchIdAndNameDto> watchList) {
        return WatchWithHostDto.builder()
                .host(host)
                .watchList(watchList)
                .build();
    }

}
