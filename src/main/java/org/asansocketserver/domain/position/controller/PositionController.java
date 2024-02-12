package org.asansocketserver.domain.position.controller;


import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.position.dto.PosDataDTO;
import org.asansocketserver.domain.position.dto.StateDTO;
import org.asansocketserver.domain.position.service.PositionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/receiveData")
    public ResponseEntity<?> receiveData(@RequestBody PosDataDTO posData) {
            positionService.receiveData(posData);
            return ResponseEntity.of(Optional.of(Map.of("receive", "success")));

    }

    @DeleteMapping("/deleteBeacon")
        public ResponseEntity<?> deleteBeacon(PositionDTO positionDTO) {
            positionService.deleteBeacon(positionDTO.getPosition());
            return ResponseEntity.of(Optional.of(Map.of("delete", "success")));
        }

    @PostMapping("/insertState")
    public ResponseEntity<?> insertState(@RequestBody StateDTO stateDTO) {

            positionService.insertState(stateDTO);
            return ResponseEntity.of(Optional.of(Map.of("status changing", "success")));
    }

    @PostMapping("/deleteState")
    public ResponseEntity<?> deleteState(@RequestBody StateDTO stateDTO) {

            positionService.deleteState(stateDTO);
            return ResponseEntity.of(Optional.of(Map.of("status changing", "success")));
    }



}



