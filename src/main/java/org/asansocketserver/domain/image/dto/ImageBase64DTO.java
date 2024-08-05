package org.asansocketserver.domain.image.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ImageBase64DTO {
    private String imageData;
}