package org.asansocketserver.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.notification.dto.request.response.NotificationResponseDto;
import org.asansocketserver.domain.notification.service.NotificationService;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/getAllNotifications")
    public ResponseEntity<SuccessResponse<?>> getAllNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String watchName,
            @RequestParam(required = false) String watchId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "false") boolean sortAsc
    ) {
        List<NotificationResponseDto> notifications = notificationService.getNotifications(page, size, type, watchName, watchId, date, sortAsc);
        return SuccessResponse.ok(notifications);
    }


    @GetMapping("/downloadNotificationsAsCsvZip")
    public ResponseEntity<byte[]> downloadNotificationsAsCsvZip(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "1000") int chunkSize
    ) throws IOException {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByDate(date);
        byte[] zipBytes = notificationService.makeZipFile(notifications,chunkSize);

        // HTTP 헤더를 설정하고 ZIP 파일을 클라이언트로 전송
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "notifications.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .body(zipBytes);
    }


}
