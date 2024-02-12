package org.asansocketserver.domain.image.repository;


import org.asansocketserver.domain.image.entity.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.asansocketserver.domain.image.entity.Image;

public interface CoordinateRepository extends JpaRepository<Coordinate,Long> {

    Coordinate findByImageIdAndPosition(Image image, String position);
}
