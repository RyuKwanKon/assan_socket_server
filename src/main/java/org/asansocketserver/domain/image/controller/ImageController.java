package org.asansocketserver.domain.image.controller;


import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.image.dto.*;
import org.asansocketserver.domain.image.service.ImageService;
import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/image")
@RestController
public class ImageController {
    private final ImageService imageService;

    // --------------- 공통 ----------------
    //이미지 조회 api
    @GetMapping("/getImage/{id}")
    public ResponseEntity<SuccessResponse<?>> getImage(@PathVariable Long id) {
        ImageResponseDto responseDto = imageService.getImage(id);
        System.out.println("responseDto = " + responseDto);
        return SuccessResponse.ok(responseDto);
    }



    //이미지 목록 조회 api -> RequestParam 추가
    @GetMapping("/getImageList")
    public ResponseEntity<SuccessResponse<?>> getImageList(@RequestParam("isWeb") Boolean isWeb)  {
        System.out.println("isWeb = " + isWeb);
        ImageListDTO responseDto = imageService.getImageList(isWeb);
//        System.out.println("responseDto = " + responseDto.getImageIds());
        return SuccessResponse.ok(responseDto);
    }


    @PostMapping("/nameChange")
    public ResponseEntity<SuccessResponse<?>> nameChange(@RequestBody ImageIdAndNameDTO imageIdAndNameDTO ) {
        Long imageId = imageService.nameChange(imageIdAndNameDTO);
        return SuccessResponse.ok(imageId);
    }


    //이미지 삭제 api
    @DeleteMapping("/deleteImage/{imageId}")
    public ResponseEntity<SuccessResponse<?>> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return SuccessResponse.ok(null);
    }


    //이미지내에 설정한 위치들의 이름 목록을 가져오는 api (앱에서 비콘을 모으기 위해 위치 목록을 띄울 경우 사용)
    @GetMapping("/getPositionList")
    public ResponseEntity<SuccessResponse<?>> getPositionList(@RequestParam("isWeb") Boolean isWeb) {
        List<PositionDTO> positionList = imageService.getPositionList(isWeb);
        return SuccessResponse.ok(positionList);
    }


    //이미지내에 설정한 위치들과 해당 좌표 목록을 가져오는 api
    @GetMapping("/getPositionAndCoordinateList/{id}")
    public ResponseEntity<SuccessResponse<?>> getPositionAndCoordinateList(@PathVariable Long id,@RequestParam("isWeb") Boolean isWeb) {
        List<CoordinateDTO> positionList = imageService.getPositionAndCoordinateList(id,isWeb);
        return SuccessResponse.ok(positionList);
    }


    //이미지 내 위치 및 범위 생성 api
    //LabelDataDTO에 isWeb 속성 존재
    @PostMapping("/postImagePositionAndCoordinates")
    public ResponseEntity<SuccessResponse<?>> saveImagePositionAndCoordinates(@RequestBody LabelDataDTO labelDataDTO) {
        imageService.saveImagePositionAndCoordinates(labelDataDTO);
        return SuccessResponse.ok(null);

    }



    // --------------- 앱 ----------------

    //이미지 저장 api
    @PostMapping("/saveImage")
    public ResponseEntity<SuccessResponse<?>> saveImage(@RequestParam("imageData") MultipartFile file) throws IOException {
        Long imageId = imageService.saveImage(file);
        System.out.println("imageId = " + imageId);
        return SuccessResponse.ok(imageId);
    }

    //이미지 내 위치 및 범위 삭제 api

    @DeleteMapping("/deleteImagePositionAndCoordinates/{positionName}")
    public ResponseEntity<SuccessResponse<?>> deleteImagePositionAndCoordinates(@PathVariable String positionName) {
        imageService.deleteImagePositionAndCoordinates(positionName);
        return SuccessResponse.ok(null);
    }

    // --------------- 웹 ----------------

    //웹 - 이미지 저장 api (flutter-web은 MultipartFile 미지원)
    @PostMapping("/web/saveImage")
    public ResponseEntity<SuccessResponse<?>> saveImageForWeb(@RequestBody ImageBase64DTO imageBase64Dto) throws IOException {
        Long imageId = imageService.saveImageForWeb(imageBase64Dto.getImageData());
//        System.out.println("imageId = " + imageId);
        return SuccessResponse.ok(imageId);
    }

    //웹 - 이미지 내 위치 및 범위 삭제 api(웹은 좌표만 삭제)
    @DeleteMapping("/web/deleteImagePositionAndCoordinates/{coorId}")
    public ResponseEntity<SuccessResponse<?>> deleteImagePositionAndCoordinatesForWeb(@PathVariable Long coorId) {
        imageService.deleteImagePositionAndCoordinatesForWeb(coorId);
        return SuccessResponse.ok(null);
    }

    //이미지 별로 지정된 위치를 목록으로 전달
    @GetMapping("/web/getImageWithPositionNameList")
    public ResponseEntity<SuccessResponse<?>> getImageWithPositionNameList()  {
        List<ImageAndCoordinateDTO> responseDto = imageService.getImageAndPositionNameList();
        return SuccessResponse.ok(responseDto);
    }

}

