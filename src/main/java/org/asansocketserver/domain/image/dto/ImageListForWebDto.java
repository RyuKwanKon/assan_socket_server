package org.asansocketserver.domain.image.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ImageListForWebDto(
         List<Long> imageIds,
         List<String> imageNames,
         List<String> imageUrls
) {
    public static ImageListForWebDto of(List<Long> imageIds, List<String> imageNames ,List<String> imageUrls) {
        return ImageListForWebDto.builder()
                .imageIds(imageIds)
                .imageNames(imageNames)
                .imageUrls(imageUrls)
                .build();
    }
}
