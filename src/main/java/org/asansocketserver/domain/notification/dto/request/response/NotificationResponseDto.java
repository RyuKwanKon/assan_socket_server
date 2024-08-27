package org.asansocketserver.domain.notification.dto.request.response;

import org.asansocketserver.domain.notification.entity.Notification;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long watchId,
        Long imageId,
        String watchName,
        String watchHost,
        String position,
        String alarmType,
        LocalDateTime timestamp
) {
    public static NotificationResponseDto fromEntity(Notification notification) {
        return new NotificationResponseDto(
                notification.getWatchId(),
                notification.getImageId(),
                notification.getWatchName(),
                notification.getWatchHost(),
                notification.getPosition(),
                notification.getAlarmType(),
                notification.getTimestamp()
        );
    }
}
