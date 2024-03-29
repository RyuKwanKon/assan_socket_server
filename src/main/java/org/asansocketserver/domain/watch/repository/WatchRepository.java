package org.asansocketserver.domain.watch.repository;

import org.asansocketserver.domain.watch.entity.Watch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatchRepository extends JpaRepository<Watch, Long> {
    Optional<Watch> findByUuid(String uuid);
    boolean existsByUuid(String uuid);
}
