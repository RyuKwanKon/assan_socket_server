package org.asansocketserver.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asansocketserver.domain.watch.dto.response.WatchAllResponseDto;
import org.asansocketserver.domain.watch.dto.response.WatchResponseDto;
import org.asansocketserver.domain.watch.entity.WatchLive;
import org.asansocketserver.domain.watch.reponsitory.WatchLiveRepository;
import org.asansocketserver.domain.watch.reponsitory.WatchRepository;
import org.asansocketserver.socket.dto.MessageType;
import org.asansocketserver.socket.dto.SocketBaseResponse;
import org.asansocketserver.socket.error.SocketException;
import org.asansocketserver.socket.error.SocketNotFoundException;
import org.asansocketserver.socket.error.SocketUnauthorizedException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.asansocketserver.socket.error.SocketErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompInterceptor implements ChannelInterceptor {
    private final static Long monitoringId = 9999999L;
    private final WatchRepository watchRepository;
    private final WatchLiveRepository watchLiveRepository;
    private final SimpMessageSendingOperations sendingOperations;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if (StompCommand.CONNECT.equals(command)) {
            Long watchId = getWatchByAuthorizationHeader(accessor);
            if (!watchId.equals(monitoringId)) {
                setWatchIdFromStompHeader(accessor, watchId);
                createWatchLiveAndSave(watchId);
            }
            log.info("[CONNECT]:: watchId : " + watchId);
        } else if (StompCommand.SUBSCRIBE.equals(command)) {
            broadcastWatchList();
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
        validateAuthorizationHeader(authHeaderValue);
        validateExistWatch(authHeaderValue);
        return Long.parseLong(authHeaderValue);
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
        sessionAttributes.put("watchId", value);
    }

    private void deleteWatchIdFromStompHeader(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
        sessionAttributes.remove("watchId");
    }

    private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (Objects.isNull(sessionAttributes)) {
            throw new SocketException(SESSION_ATTRIBUTE_NOT_FOUND);
        }
        return sessionAttributes;
    }

    private void broadcastWatchList() {
        WatchAllResponseDto responseDto = findAllWatch();
        sendingOperations.convertAndSend("/queue/watchList", SocketBaseResponse.of(MessageType.WATCH_LIST, responseDto));
    }

    private WatchAllResponseDto findAllWatch() {
        List<WatchLive> watchLiveList = findAllWatchInRedis();
        List<WatchResponseDto> watchResponseDtoList = WatchResponseDto.liveListOf(watchLiveList);
        return WatchAllResponseDto.of(watchResponseDtoList);
    }

    private void createWatchLiveAndSave(Long watchId) {
        WatchLive watchLive = WatchLive.createWatchLive(watchId);
        watchLiveRepository.save(watchLive);
    }

    private void validateAuthorizationHeader(String authHeaderValue) {
        if (Objects.isNull(authHeaderValue) || authHeaderValue.isBlank())
            throw new SocketUnauthorizedException(BLINK_AUTHORIZED_HEADER);
    }

    private void validateExistWatch(String authHeaderValue) {
        if (!watchRepository.existsById(Long.parseLong(authHeaderValue)))
            throw new SocketNotFoundException(WATCH_NOT_FOUND);
    }

    private List<WatchLive> findAllWatchInRedis() {
        return watchLiveRepository.findAllByLive(true);
    }

    private boolean existWatchInRedis(Long watchId) {
        return watchLiveRepository.existsById(watchId);
    }

    private void deleteWatchInRedis(Long watchId) {
        watchLiveRepository.deleteById(watchId);
    }
}

