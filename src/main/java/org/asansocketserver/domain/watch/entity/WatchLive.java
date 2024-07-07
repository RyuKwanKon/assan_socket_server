package org.asansocketserver.domain.watch.entity;

import lombok.*;
import org.asansocketserver.domain.watch.dto.request.WatchUpdateRequestDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@RedisHash(value = "watch")
public class WatchLive {
    @Id
    private Long id;
    private String watchName;
    @Indexed
    private boolean live;

    public static WatchLive createWatchLive(Long watchId, String watchName) {
        return WatchLive.builder()
                .id(watchId)
                .watchName(watchName)
                .live(true)
                .build();
    }

    public void updateWatchLiveName(WatchUpdateRequestDto requestDto) {
        this.watchName = requestDto.name();

    }
}
