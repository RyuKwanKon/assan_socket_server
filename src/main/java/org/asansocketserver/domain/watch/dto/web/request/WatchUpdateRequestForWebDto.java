package org.asansocketserver.domain.watch.dto.web.request;

import java.util.List;

public record WatchUpdateRequestForWebDto(
        String device,
        String name,
        String host,
        String gender,
        String highrisk,
        List<Long> accessibleAreaList,
        List<Long> noContactPatientList
) {
}
