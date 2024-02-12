package org.asansocketserver.domain.image.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelDataDTO {

    private Long imageId;
    private String position;
    private float startX;
    private float startY;
    private float endX;
    private float endY;


}

