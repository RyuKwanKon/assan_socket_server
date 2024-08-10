package org.asansocketserver.domain.watch.dto.web.request;

import lombok.Builder;

import java.util.List;

@Builder
public record WatchProhibitedCoordinatesUpdateRequestDto(
        Long watchId,
        List<Long> prohibitedCoordinatesIds) {
}
