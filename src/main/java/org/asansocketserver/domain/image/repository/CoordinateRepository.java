package org.asansocketserver.domain.image.repository;


import org.asansocketserver.domain.image.entity.Coordinate;
import org.asansocketserver.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    Coordinate findByImageIdAndPosition(Image image, String position);
}
