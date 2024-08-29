package org.asansocketserver.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.notification.dto.request.NotificationRequestDto;
import org.asansocketserver.domain.notification.dto.request.response.NotificationResponseDto;
import org.asansocketserver.domain.notification.entity.Notification;
import org.asansocketserver.domain.notification.mongorepository.NotificationMongoRepository;
import org.asansocketserver.domain.watch.entity.Watch;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final CacheManager cacheManager;
    private final NotificationMongoRepository notificationMongoRepository;



    public void createAndSaveNotification(Watch watch, Long imageId, String prediction, String alarmType) {
        logCacheStatus();
        LocalDateTime now = LocalDateTime.now();
        String cacheKey = createCacheKey(watch.getId(), alarmType);

        Cache cache = cacheManager.getCache("notifications");
        if (cache != null && cache instanceof ConcurrentMapCache) {
            ConcurrentMap<Object, Object> nativeCache = ((ConcurrentMapCache) cache).getNativeCache();

            LocalDateTime cachedTimestamp = (LocalDateTime) nativeCache.get(cacheKey);
            if (cachedTimestamp == null || cachedTimestamp.plusMinutes(1).isBefore(now)) {
                nativeCache.put(cacheKey, now);

                NotificationRequestDto requestDto = NotificationRequestDto.of(
                        watch.getId(),
                        imageId,
                        watch.getName(),
                        watch.getHost(),
                        prediction,
                        alarmType,
                        now
                );

                Notification notification = Notification.createNotification(requestDto);
                notificationMongoRepository.save(notification);

                log.info("알림 저장: " + requestDto);
            } else {
                log.info("이미 동일한 알림이 저장됨: " + cacheKey);
            }
        }
    }

    private String createCacheKey(Long watchId, String alarmType) {
        return watchId + ":" + alarmType;
    }


    public void logCacheStatus() {
        Cache cache = cacheManager.getCache("notifications");
        if (cache != null && cache instanceof ConcurrentMapCache) {
            ConcurrentMap<Object, Object> nativeCache = ((ConcurrentMapCache) cache).getNativeCache();

            log.info("현재 캐시 상태:");
            nativeCache.forEach((key, value) -> System.out.println("Key: {}, Value: {}" + key + value));
        } else {
            log.info("캐시가 존재하지 않거나 ConcurrentMapCache가 아닙니다.");
        }
    }


    public List<NotificationResponseDto> getNotifications(int page, int size, String type, String watchName, String watchId, LocalDate startDate, LocalDate endDate , boolean sortAsc) {
        int skip = (page - 1) * size;

        // 지정된 날짜의 시작과 끝을 설정하거나, 날짜가 없으면 오늘 날짜를 사용
        LocalDateTime startOfDay;
        LocalDateTime endOfDay;

        if (startDate != null && endDate != null) {
            startOfDay = startDate.atStartOfDay();
            System.out.println("startOfDay = " + startOfDay);
            endOfDay = endDate.atTime(LocalTime.MAX);
            System.out.println("endOfDay = " + endOfDay);
        } else {
            LocalDate today = LocalDate.now();
            startOfDay = today.atStartOfDay();
            endOfDay = today.atTime(LocalTime.MAX);
        }

        List<Notification> filteredNotifications = notificationMongoRepository.findAll().stream()
                .filter(notification -> type == null || notification.getAlarmType().equals(type))
                .filter(notification -> watchName == null || notification.getWatchName().equals(watchName))
                .filter(notification -> watchId == null || notification.getWatchId().toString().equals(watchId))
                .filter(notification -> !notification.getTimestamp().isBefore(startOfDay) && !notification.getTimestamp().isAfter(endOfDay))
                .sorted((n1, n2) -> {
                    int comparison = n1.getTimestamp().compareTo(n2.getTimestamp());
                    return sortAsc ? comparison : -comparison;
                })
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());

        return filteredNotifications.stream()
                .map(NotificationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<NotificationResponseDto> getNotificationsByDate(LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);


        List<Notification> notifications = notificationMongoRepository.findALLByTimestamp(startOfDay, endOfDay);

        // 각 Notification 엔티티를 NotificationResponseDto로 변환하여 반환합니다.
        return notifications.stream()
                .map(NotificationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }



    public byte[] makeZipFile(List<NotificationResponseDto> notifications, int chunkSize) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        int fileNumber = 1;
        for (int i = 0; i < notifications.size(); i += chunkSize) {
            int toIndex = Math.min(i + chunkSize, notifications.size());
            List<NotificationResponseDto> chunk = notifications.subList(i, toIndex);

            // CSV 파일을 생성
            StringBuilder csvContent = new StringBuilder();
            String bom = "\uFEFF"; // UTF-8 BOM 추가
            csvContent.append(bom); // BOM을 파일의 시작에 추가
            csvContent.append("WatchId,WatchName,WatchHost,Position,Type,Timestamp\n"); // 헤더
            for (NotificationResponseDto notification : chunk) {
                csvContent.append(notification.watchId() != null ? notification.watchId() : "").append(",")
                        .append(notification.watchName() != null ? notification.watchName() : "").append(",")
                        .append(notification.watchHost() != null ? notification.watchHost() : "").append(",")
                        .append(notification.position() != null ? notification.position() : "").append(",")
                        .append(notification.alarmType() != null ? notification.alarmType() : "").append(",")
                        .append(notification.timestamp() != null ? notification.timestamp() : "").append("\n");
            }

            // CSV 파일을 ZIP에 추가
            String fileName = "notifications_" + fileNumber + ".csv";
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(csvContent.toString().getBytes(StandardCharsets.UTF_8));
            zipOutputStream.closeEntry();

            fileNumber++;
        }

        // ZIP 스트림을 마무리
        zipOutputStream.finish();
        zipOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public List<NotificationResponseDto> getNotificationsByDateForDownload(String type, String watchName, String watchId, LocalDate startDate, LocalDate endDate,boolean sortAsc) {

        LocalDateTime startOfDay;
        LocalDateTime endOfDay;

        if (startDate != null && endDate != null) {
            startOfDay = startDate.atStartOfDay();
            System.out.println("startOfDay = " + startOfDay);
            endOfDay = endDate.atTime(LocalTime.MAX);
            System.out.println("endOfDay = " + endOfDay);
        } else {
            LocalDate today = LocalDate.now();
            startOfDay = today.atStartOfDay();
            endOfDay = today.atTime(LocalTime.MAX);
        }

        List<Notification> filteredNotifications = notificationMongoRepository.findAll().stream()
                .filter(notification -> type == null || notification.getAlarmType().equals(type))
                .filter(notification -> watchName == null || notification.getWatchName().equals(watchName))
                .filter(notification -> watchId == null || notification.getWatchId().toString().equals(watchId))
                .filter(notification -> !notification.getTimestamp().isBefore(startOfDay) && !notification.getTimestamp().isAfter(endOfDay))
                .sorted((n1, n2) -> {
                    int comparison = n1.getTimestamp().compareTo(n2.getTimestamp());
                    return sortAsc ? comparison : -comparison;
                })
                .collect(Collectors.toList());

        return filteredNotifications.stream()
                .map(NotificationResponseDto::fromEntity)
                .collect(Collectors.toList());

    }
}