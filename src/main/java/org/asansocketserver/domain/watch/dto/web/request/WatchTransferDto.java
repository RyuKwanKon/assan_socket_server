package org.asansocketserver.domain.watch.dto.web.request;

public record WatchTransferDto(
        Long sendInfoId,
        Long receiveInfoId
) {
}
