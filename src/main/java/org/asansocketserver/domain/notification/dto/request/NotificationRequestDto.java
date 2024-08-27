package org.asansocketserver.domain.notification.dto.request;

import java.time.LocalDateTime;

public record NotificationRequestDto(
        Long watchId,
        Long imageId,
        String watchName,
        String watchHost,
        String position,
        String alarmType,
        LocalDateTime timeStamp
) {
    public static NotificationRequestDto of(Long watchId, Long imageId, String watchName, String watchHost,
                                            String position, String alarmType, LocalDateTime timeStamp) {
        return new NotificationRequestDto(watchId, imageId, watchName, watchHost, position, alarmType, timeStamp);
    }
}
