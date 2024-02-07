package org.asansocketserver.domain.watch.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.watch.dto.request.WatchRequestDto;
import org.asansocketserver.domain.watch.dto.response.WatchAllResponseDto;
import org.asansocketserver.domain.watch.dto.response.WatchResponseDto;
import org.asansocketserver.domain.watch.service.WatchService;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/watch")
@RestController
public class WatchApiController {
    private final WatchService watchService;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> findAllWatch() {
        final WatchAllResponseDto responseDto = watchService.findAllWatch();
        return SuccessResponse.ok(responseDto);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<SuccessResponse<?>> findWatch(@PathVariable final String uuid) {
        final WatchResponseDto responseDto = watchService.findWatch(uuid);
        return SuccessResponse.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createWatch(@RequestBody final WatchRequestDto watchRequestDto) {
        final WatchResponseDto responseDto = watchService.createWatch(watchRequestDto);
        return SuccessResponse.created(responseDto);
    }
}
