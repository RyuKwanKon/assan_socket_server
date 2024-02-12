package org.asansocketserver.domain.image.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    private String imageUrl;

    @OneToMany(mappedBy = "imageId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coordinate> coordinates = new ArrayList<>();
}

