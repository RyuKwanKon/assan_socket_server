package org.asansocketserver.domain.watch.dto.web.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.asansocketserver.domain.watch.entity.Watch;

import java.util.List;
import java.util.stream.Collectors;

@Builder(access = AccessLevel.PRIVATE)
public record WatchResponseForWebDto(
        Long watchId,
        String device,
        String name,
        String host,
        String gender,
        String highrisk,
        List<Long> accessibleAreaList,
        List<Long> noContactPatientList
) {
    public static WatchResponseForWebDto of(Watch watch) {
        return WatchResponseForWebDto.builder()
                .watchId(watch.getId())
                .device(watch.getDevice())
                .name(watch.getName())
                .host(watch.getHost())
                .gender(watch.getGender().toString())
                .highrisk(watch.getHighRisk().toString())
                .accessibleAreaList(watch.getAccessibleAreaList())
                .noContactPatientList(watch.getNoContactPatientList())
                .build();
    }

    public static List<WatchResponseForWebDto> listOf(List<Watch> watchList) {
        return watchList.stream()
                .map(WatchResponseForWebDto::of)
                .collect(Collectors.toList());
    }
}
