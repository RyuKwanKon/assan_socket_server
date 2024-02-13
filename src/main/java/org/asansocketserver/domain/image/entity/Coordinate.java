package org.asansocketserver.domain.image.entity;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "coordinate")
@Entity
public class Coordinate {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image imageId;
    private String position;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
}
