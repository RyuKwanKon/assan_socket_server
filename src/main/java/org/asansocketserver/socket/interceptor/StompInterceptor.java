package org.asansocketserver.socket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.position.entity.Position;
import org.asansocketserver.domain.position.mongorepository.PositionMongoRepository;
import org.asansocketserver.domain.sensor.entity.*;
import org.asansocketserver.domain.sensor.mongorepository.*;
import org.asansocketserver.domain.watch.entity.WatchLive;
import org.asansocketserver.domain.watch.repository.WatchLiveRepository;
import org.asansocketserver.domain.watch.repository.WatchRepository;
import org.asansocketserver.socket.error.SocketException;
import org.asansocketserver.socket.error.SocketNotFoundException;
import org.asansocketserver.socket.error.SocketUnauthorizedException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import static org.asansocketserver.socket.error.SocketErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {
    public final static Long monitoringId = 9999999L;
    private final WatchRepository watchRepository;
    private final SensorAccelerometerRepository sensorAccelerometerRepository;
    private final SensorBarometerRepository sensorBarometerRepository;
    private final SensorGyroscopeRepository sensorGyroscopeRepository;
    private final SensorHeartRateRepository sensorHeartRateRepository;
    private final SensorLightRepository sensorLightRepository;
    private final PositionMongoRepository positionMongoRepository;
    private final WatchLiveRepository watchLiveRepository;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if (StompCommand.CONNECT.equals(command)) {
            Long watchId = getWatchByAuthorizationHeader(accessor);
            setWatchIdFromStompHeader(accessor, watchId);
            if (!watchId.equals(monitoringId)) {
                createWatchLiveAndSave(watchId);
                createAccelerometerAndSave(watchId);
                createBarometerAndSave(watchId);
                createGyroscopeAndSave(watchId);
                createHeartRateAndSave(watchId);
                createLightAndSave(watchId);
                createPositionAndSave(watchId);
            }
            log.info("[CONNECT]:: watchId : " + watchId);
        } else if (StompCommand.DISCONNECT.equals(command)) {
            Long watchId = (Long) getWatchIdFromStompHeader(accessor);
            if (existWatchInRedis(watchId) && !watchId.equals(monitoringId)) {
                deleteWatchIdFromStompHeader(accessor);
                deleteWatchInRedis(watchId);
            }
            log.info("DISCONNECTED watchId : {}", watchId);
        }
        return message;
    }

    private Long getWatchByAuthorizationHeader(StompHeaderAccessor accessor) {
        String authHeaderValue = accessor.getFirstNativeHeader("Authorization");
        long longAuthHeaderValue = Long.parseLong(Objects.requireNonNull(authHeaderValue));
        if (longAuthHeaderValue != monitoringId) {
            validateAuthorizationHeader(authHeaderValue);
            validateExistWatch(authHeaderValue);
        }
        return longAuthHeaderValue;
    }

    private Object getWatchIdFromStompHeader(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        Object value = sessionAttributes.get("watchId");
        if (Objects.isNull(value))
            throw new SocketException(SOCKET_SERVER_ERROR);
        return value;
    }

    private void setWatchIdFromStompHeader(StompHeaderAccessor accessor, Object value) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        if (Objects.isNull(sessionAttributes)) return;
        sessionAttributes.put("watchId", value);
    }

    private void deleteWatchIdFromStompHeader(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        if (Objects.isNull(sessionAttributes)) return;
        sessionAttributes.remove("watchId");
    }

    private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (Objects.isNull(sessionAttributes))
            return null;
        return sessionAttributes;
    }

    private void createWatchLiveAndSave(Long watchId) {
        WatchLive watchLive = WatchLive.createWatchLive(watchId);
        watchLiveRepository.save(watchLive);
    }

    private void createAccelerometerAndSave(Long watchId) {
        if (sensorAccelerometerRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        SensorAccelerometer sensorAccelerometer = SensorAccelerometer.createSensor(watchId);
        sensorAccelerometerRepository.save(sensorAccelerometer);
    }

    private void createBarometerAndSave(Long watchId) {
        if (sensorBarometerRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        SensorBarometer sensorBarometer = SensorBarometer.createSensor(watchId);
        sensorBarometerRepository.save(sensorBarometer);
    }

    private void createGyroscopeAndSave(Long watchId) {
        if (sensorGyroscopeRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        SensorGyroscope sensorGyroscope = SensorGyroscope.createSensor(watchId);
        sensorGyroscopeRepository.save(sensorGyroscope);
    }

    private void createHeartRateAndSave(Long watchId) {
        if (sensorHeartRateRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        SensorHeartRate sensorHeartRate = SensorHeartRate.createSensor(watchId);
        sensorHeartRateRepository.save(sensorHeartRate);
    }

    private void createLightAndSave(Long watchId) {
        if (sensorLightRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        SensorLight sensorLight = SensorLight.createSensor(watchId);
        sensorLightRepository.save(sensorLight);
    }

    private void createPositionAndSave(Long watchId) {
        if (positionMongoRepository.existsByWatchIdAndDate(watchId, LocalDate.now())) return;
        Position position = Position.of(watchId);
        positionMongoRepository.save(position);
    }

    private void validateAuthorizationHeader(String authHeaderValue) {
        if (Objects.isNull(authHeaderValue) || authHeaderValue.isBlank())
            throw new SocketUnauthorizedException(BLINK_AUTHORIZED_HEADER);
    }

    private void validateExistWatch(String authHeaderValue) {
        if (!watchRepository.existsById(Long.parseLong(authHeaderValue)))
            throw new SocketNotFoundException(WATCH_NOT_FOUND);
    }

    private boolean existWatchInRedis(Long watchId) {
        return watchLiveRepository.existsById(watchId);
    }

    private void deleteWatchInRedis(Long watchId) {
        watchLiveRepository.deleteById(watchId);
    }
}

