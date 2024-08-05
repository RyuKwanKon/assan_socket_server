package org.asansocketserver.domain.image.repository;

import org.asansocketserver.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByIsWebTrue();

    List<Image> findAllByIsWebFalse();
}
