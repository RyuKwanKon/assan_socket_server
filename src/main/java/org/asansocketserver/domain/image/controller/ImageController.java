package org.asansocketserver.domain.image.controller;


import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.image.dto.CoordinateDTO;
import org.asansocketserver.domain.image.dto.ImageDataDTO;
import org.asansocketserver.domain.image.dto.ImageResponseDto;
import org.asansocketserver.domain.image.dto.LabelDataDTO;
import org.asansocketserver.domain.image.service.ImageService;
import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/image")
@RestController
public class ImageController {
    private final ImageService imageService;

    //이미지 조회 api
    @GetMapping("/getImage")
    public ResponseEntity<SuccessResponse<?>> getImage() {
        ImageResponseDto responseDto = imageService.getImage();
        return SuccessResponse.ok(responseDto);
    }


    //이미지 저장 api
    @PostMapping("/saveImage")
    public ResponseEntity<SuccessResponse<?>> saveImage(@RequestBody ImageDataDTO imageDataDTO) {
        imageService.saveImage(imageDataDTO.getImageUrl());
        return SuccessResponse.ok(null);
    }


    //이미지 삭제 api
    @DeleteMapping("/deleteImage")
    public ResponseEntity<SuccessResponse<?>> deleteImage(@RequestBody ImageDataDTO imageDataDTO) {
        Long ImageId = imageDataDTO.getImageId();
        imageService.deleteImage(ImageId);
        return SuccessResponse.ok(null);
    }


    //이미지내에 설정한 위치들의 목록을 가져오는 api
    @GetMapping("/getPositionList")
    public ResponseEntity<SuccessResponse<?>> getPositionList() {
        List<PositionDTO> positionList = imageService.getPositionList();
        return SuccessResponse.ok(positionList);
    }


    //이미지내에 설정한 위치들과 해당 좌표 목록을 가져오는 api
    @GetMapping("/getPositionAndCoordinateList")
    public ResponseEntity<SuccessResponse<?>> getPositionAndCoordinateList() {
        List<CoordinateDTO> positionList = imageService.getPositionAndCoordinateList();
        return SuccessResponse.ok(positionList);
    }


    //이미지 내 위치 및 범위 생성 api
    @PostMapping("/postImagePositionAndCoordinates")
    public ResponseEntity<SuccessResponse<?>> saveImagePositionAndCoordinates(@RequestBody LabelDataDTO labelDataDTO) {
        imageService.saveImagePositionAndCoordinates(labelDataDTO);
        return SuccessResponse.ok(null);

    }

    //이미지 내 위치 및 범위 삭제 api
    @DeleteMapping("/deleteImagePositionAndCoordinates/{coordinateId}")
    public ResponseEntity<SuccessResponse<?>> deleteImagePositionAndCoordinates(@PathVariable Long coordinateId) {
        imageService.deleteImagePositionAndCoordinates(coordinateId);
        return SuccessResponse.ok(null);
    }
}

