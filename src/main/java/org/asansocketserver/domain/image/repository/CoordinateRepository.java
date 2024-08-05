package org.asansocketserver.domain.image.repository;


import org.asansocketserver.domain.image.entity.Coordinate;
import org.asansocketserver.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    Coordinate findByImageIdAndPosition(Image image, String position);

    Coordinate findByPosition( String position);

    List<Coordinate> findAllByImageIdAndIsWebFalse(Image image);

    List<Coordinate> findAllByImageIdAndIsWebTrue(Image image);

    List<Coordinate> findAllByIsWebTrue();

    List<Coordinate> findAllByIsWebFalse();
}
