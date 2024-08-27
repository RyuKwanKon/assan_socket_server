package org.asansocketserver.domain.image.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ImageIDAndNameAndCoordinateDTO(
        Long imageId,
        String imageName,
        Long coordinateId,
        String position

) {
//    public static ImageIDAndNameAndCoordinateDTO of(Long imageId, String imageName, String position) {
//        return ImageIDAndNameAndCoordinateDTO.builder()
//                .imageId(imageId)
//                .imageName(imageName)
//                .position(position)
//                .build();
}


