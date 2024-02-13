package org.asansocketserver.domain.watch.dto.request;

public record WatchUpdateRequestDto(
        String name,
        String host
) {
}
