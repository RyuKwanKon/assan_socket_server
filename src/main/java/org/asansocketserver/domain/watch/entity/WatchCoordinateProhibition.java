package org.asansocketserver.domain.watch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.asansocketserver.domain.image.entity.Coordinate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "watch_coordinate_restriction")
@Entity
public class WatchCoordinateProhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "watch_id")
    private Watch watch;

    @ManyToOne
    @JoinColumn(name = "coordinate_id")
    private Coordinate coordinate;

    // 생성 메서드
    public static WatchCoordinateProhibition createProhibition(Watch watch, Coordinate coordinate) {
        return WatchCoordinateProhibition.builder()
                .watch(watch)
                .coordinate(coordinate)
                .build();
    }
}
