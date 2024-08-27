package org.asansocketserver.domain.image.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.asansocketserver.domain.watch.dto.web.response.WatchIdAndNameDto;

import java.util.List;


@Builder(access = AccessLevel.PRIVATE)
public record CoordinateIDAndPositionDTO(
        Long coordinateId,
        String position

) {
    public static CoordinateIDAndPositionDTO of(Long coordinateId, String position) {
        return CoordinateIDAndPositionDTO.builder()
                .coordinateId(coordinateId)
                .position(position)
                .build();
    }

}
