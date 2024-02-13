package org.asansocketserver.domain.watch.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.entity.WatchLive;

import java.util.List;
import java.util.stream.Collectors;

@Builder(access = AccessLevel.PRIVATE)
public record WatchResponseDto(
        Long watchId,
        String name,
        String host
) {
    public static WatchResponseDto of(Watch watch) {
        return WatchResponseDto.builder()
                .watchId(watch.getId())
                .name(watch.getName())
                .host(watch.getHost())
                .build();
    }

    public static List<WatchResponseDto> listOf(List<Watch> watchList) {
        return watchList.stream()
                .map(WatchResponseDto::of)
                .collect(Collectors.toList());
    }
}
