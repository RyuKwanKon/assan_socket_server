package org.asansocketserver.domain.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.position.dto.request.StateDTO;
import org.asansocketserver.domain.sensor.dto.request.*;
import org.asansocketserver.domain.sensor.dto.response.*;
import org.asansocketserver.domain.sensor.service.SensorService;
import org.asansocketserver.global.common.SuccessResponse;
import org.asansocketserver.socket.dto.MessageType;
import org.asansocketserver.socket.dto.SocketBaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class SensorApiController {
    private final SensorService sensorService;



    @PostMapping("/api/sensor/sendState")
    public ResponseEntity<SuccessResponse<?>> insertState(@RequestBody StateRequestDto stateDTO) {
        sensorService.sensorSendState(stateDTO);
        return SuccessResponse.ok("success");
    }

    @PostMapping("/api/sensor/downloadSensorDataAsCsvZip")
    public ResponseEntity<byte[]> downloadSensorDataAsCsvZip(
            @RequestBody DownloadRequestDto downloadRequestDto,
            @RequestParam(defaultValue = "100000") int chunkSize
    ) throws IOException {

        byte[] zipBytes = sensorService.downloadSensorDataAsCsvZip(downloadRequestDto, chunkSize);

        // HTTP 헤더를 설정하여 ZIP 파일을 클라이언트로 전송할 준비
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "sensorData.zip");

        // HTTP 응답을 생성하여 ZIP 파일을 클라이언트에 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(zipBytes);
    }




    @MessageMapping("/accelerometer")
    public void sendAccelerometer(@Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
                                  @Payload final AccelerometerRequestDto request) {
        sensorService.sendAccelerometer(simpSessionAttributes, request);
    }

    @MessageMapping("/barometer")
    public void sendBarometer(@Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
                              @Payload final BarometerRequestDto request) {
        sensorService.sendBarometer(simpSessionAttributes, request);
//        String destination = "/queue/sensor/" + simpSessionAttributes.get("watchId");
        // sendingOperations.convertAndSend(destination, SocketBaseResponse.of(MessageType.BAROMETER, response));
    }

    @MessageMapping("/gyroscope")
    public void sendGyroscope(@Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
                              @Payload final GyroscopeRequestDto request) {
        sensorService.sendGyroscope(simpSessionAttributes, request);
//        String destination = "/queue/sensor/" + simpSessionAttributes.get("watchId");
        //sendingOperations.convertAndSend(destination, SocketBaseResponse.of(MessageType.GYROSCOPE, response));
    }

    @MessageMapping("/heart-rate")
    public void sendHeartRate(@Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
                              @Payload final HeartRateRequestDto request) {
        sensorService.sendHeartRate(simpSessionAttributes, request);
    }

    @MessageMapping("/light")
    public void sendLight(@Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
                          @Payload final LightRequestDto request) {
        sensorService.sendLight(simpSessionAttributes, request);
//        String destination = "/queue/sensor/" + simpSessionAttributes.get("watchId");
        // sendingOperations.convertAndSend(destination, SocketBaseResponse.of(MessageType.LIGHT, response));
    }


}
