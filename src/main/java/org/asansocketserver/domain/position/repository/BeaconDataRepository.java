package org.asansocketserver.domain.position.repository;

import org.asansocketserver.domain.position.entity.BeaconData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeaconDataRepository extends JpaRepository<BeaconData,Long> {
    List<BeaconData> findAllByPosition(String positionName);

    public void deleteAllByImageId(Long imageId);

    public void deleteAllByPosition(String positionName);
}
