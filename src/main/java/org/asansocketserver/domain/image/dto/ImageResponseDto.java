package org.asansocketserver.domain.image.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ImageResponseDto(
        String url
) {
    public static ImageResponseDto of(String url) {
        return ImageResponseDto.builder()
                .url(url)
                .build();
    }
}
