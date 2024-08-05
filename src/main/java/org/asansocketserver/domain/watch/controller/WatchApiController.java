package org.asansocketserver.domain.watch.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.watch.dto.request.WatchRequestDto;
import org.asansocketserver.domain.watch.dto.request.WatchUpdateRequestDto;
import org.asansocketserver.domain.watch.dto.response.WatchAllResponseDto;
import org.asansocketserver.domain.watch.dto.response.WatchResponseDto;
import org.asansocketserver.domain.watch.dto.web.request.WatchUpdateRequestForWebDto;
import org.asansocketserver.domain.watch.dto.web.response.WatchResponseForWebDto;
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponse<?>> deleteWatch(@PathVariable("id") final Long id) {
        return SuccessResponse.ok( watchService.deleteWatch(id));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<SuccessResponse<?>> findWatch(@PathVariable final String uuid) {
        final WatchResponseDto responseDto = watchService.findWatch(uuid);
        return SuccessResponse.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createWatch(@RequestBody final WatchRequestDto requestDto) {
        final WatchResponseDto responseDto = watchService.createWatch(requestDto);
        return SuccessResponse.created(responseDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<SuccessResponse<?>> updateWatchInfo(@PathVariable("id") final Long id,
                                                              @RequestBody final WatchUpdateRequestDto requestDto) {
        final WatchResponseDto responseDto = watchService.updateWatchInfo(id, requestDto);
        return SuccessResponse.created(responseDto);
    }

    @PostMapping("/web/{id}")
    public ResponseEntity<SuccessResponse<?>> updateWatchInfoForWeb(@PathVariable("id") final Long id,
                                                                    @RequestBody final WatchUpdateRequestForWebDto requestDto) {
        final WatchResponseForWebDto responseDto = watchService.updateWatchInfoForWeb(id, requestDto);
        System.out.println("responseDto = " + responseDto);
        return SuccessResponse.created(responseDto);
    }

    @GetMapping("/web/{uuid}")
    public ResponseEntity<SuccessResponse<?>> findWatchForWeb(@PathVariable final String uuid) {
        final WatchResponseForWebDto responseDto = watchService.findWatchForWeb(uuid);
        return SuccessResponse.ok(responseDto);
    }
}
