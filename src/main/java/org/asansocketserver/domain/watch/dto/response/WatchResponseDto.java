package org.asansocketserver.domain.watch.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.entity.WatchLive;

import java.util.List;
import java.util.stream.Collectors;

@Builder(access = AccessLevel.PRIVATE)
public record WatchResponseDto(
        Long watchId
) {
    public static WatchResponseDto of(Long id) {
        return WatchResponseDto.builder()
                .watchId(id)
                .build();
    }

    public static List<WatchResponseDto> listOf(List<Watch> watchList) {
        return watchList.stream()
                .map(watch -> WatchResponseDto.of(watch.getId()))
                .collect(Collectors.toList());
    }

    public static List<WatchResponseDto> liveListOf(List<WatchLive> watchLiveList) {
        return watchLiveList.stream()
                .map(watchLive -> WatchResponseDto.of(watchLive.getId()))
                .collect(Collectors.toList());
    }
}
