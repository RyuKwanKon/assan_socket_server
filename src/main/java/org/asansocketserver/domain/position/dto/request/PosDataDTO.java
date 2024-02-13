package org.asansocketserver.domain.position.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PosDataDTO(
        String android_id,
        String position,
        String password,
        @JsonProperty("beacon_data")
        List<BeaconDataDTO> beaconData
) {
}
