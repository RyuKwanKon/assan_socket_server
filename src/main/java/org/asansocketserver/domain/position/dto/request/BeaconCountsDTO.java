package org.asansocketserver.domain.position.dto.request;

public record BeaconCountsDTO(
        String position,
        int counts
) {
}