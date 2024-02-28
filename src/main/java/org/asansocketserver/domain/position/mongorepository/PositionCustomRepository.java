package org.asansocketserver.domain.position.mongorepository;

import org.asansocketserver.domain.position.entity.PositionData;

public interface PositionCustomRepository {
    void updatePosition(final Long watchId, final PositionData positionData);
}
