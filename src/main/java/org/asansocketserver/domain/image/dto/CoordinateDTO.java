package org.asansocketserver.domain.image.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoordinateDTO {
    private Long imageId;
    private Long coordinateId;
    private String position;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
}
