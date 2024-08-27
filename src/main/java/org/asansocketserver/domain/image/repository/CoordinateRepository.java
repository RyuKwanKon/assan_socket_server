package org.asansocketserver.domain.image.repository;


import org.asansocketserver.domain.image.entity.Coordinate;
import org.asansocketserver.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    Coordinate findByImageAndPosition(Image image, String position);

    Optional<Coordinate> findByPosition(String position);

    List<Coordinate> findAllByImageAndIsWebFalse(Image image);

    List<Coordinate> findAllByImageAndIsWebTrue(Image image);

    List<Coordinate> findAllByIsWebTrue();

    List<Coordinate> findAllByIsWebFalse();

    Optional<Coordinate> findByPositionAndIsWebTrue(String prediction);
}
