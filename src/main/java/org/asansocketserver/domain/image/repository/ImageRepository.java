package org.asansocketserver.domain.image.repository;

import org.asansocketserver.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
