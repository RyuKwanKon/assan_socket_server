package org.asansocketserver.domain.position.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@RedisHash(value = "position")
public class PositionState {
    @Id
    private Long id;
    private Long imageId;
    @Indexed
    private String position;
    private Long startTime;

    public static PositionState createPositionState(Long watchId, Long imageId, String position, Long startTime) {
        return PositionState.builder()
                .id(watchId)
                .imageId(imageId)
                .position(position)
                .startTime(startTime)
                .build();
    }
}
