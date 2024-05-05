package org.asansocketserver.domain.position.repository;

import org.asansocketserver.domain.position.entity.BeaconData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;
import java.util.List;

public interface BeaconDataRepository extends JpaRepository<BeaconData,Long> {
    List<BeaconData> findAllByPosition(String positionName);


    @Query("SELECT b.position, COUNT(b) FROM BeaconData b GROUP BY b.position")
    List<Object[]> findAllBeaconCount();

    public void deleteAllByImageId(Long imageId);

    public void deleteAllByPosition(String positionName);
}
