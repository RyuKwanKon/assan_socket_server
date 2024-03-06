package org.asansocketserver.domain.image.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class LabelDataDTO {

    private Long imageId;
    private String latitude;
    private String longitude;
    private String position;
    private BigDecimal startX;
    private BigDecimal startY;
    private BigDecimal endX;
    private BigDecimal endY;


}

