package org.asansocketserver.domain.watch.repository;

import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.entity.WatchNoContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchNoContactRepository extends JpaRepository<WatchNoContact, Long> {

    void deleteByWatch(Watch watch);

    void deleteByNoContactWatch(Watch noContactWatch);

    List<WatchNoContact> findByWatch(Watch watch);

    List<WatchNoContact> findByNoContactWatch(Watch noContactWatch);

    void deleteAllByWatch(Watch watch);

    void deleteAllByNoContactWatch(Watch watch);
}
