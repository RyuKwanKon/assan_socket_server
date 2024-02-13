//package org.asansocketserver.domain.image.controller;
//
//
//import lombok.RequiredArgsConstructor;
//import org.asansocketserver.domain.image.dto.CoordinateDTO;
//import org.asansocketserver.domain.image.dto.ImageDataDTO;
//import org.asansocketserver.domain.image.dto.LabelDataDTO;
//import org.asansocketserver.domain.image.service.ImageService;
//import org.asansocketserver.domain.position.dto.PositionDTO;
//import org.asansocketserver.global.common.ResponseMessage;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RequestMapping("/api/image")
//@RestController
//public class ImageController {
//    private final ImageService imageService;
//
//    //이미지 조회 api
//    @GetMapping("/getImage")
//    public ResponseEntity<ResponseMessage> getImage(){
//
//        try {
//            String imageUrl = imageService.getImage();
//            ResponseMessage message = new ResponseMessage(200, "지원한 내역 리스트 조회 완료", imageUrl);
//            return new ResponseEntity<>(message, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            ResponseMessage message = new ResponseMessage(400,  e.getMessage());
//            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//        }
//    }
//
//
//    //이미지 저장 api
//    @PostMapping("/saveImage")
//    public ResponseEntity<ResponseMessage> saveImage(@RequestBody ImageDataDTO imageDataDTO){
//        try {
//            imageService.saveImage(imageDataDTO.getImageUrl());
//            ResponseMessage message = new ResponseMessage(200, "이미지 저장 완료");
//            return new ResponseEntity<>(message, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            ResponseMessage message = new ResponseMessage(400,  e.getMessage());
//            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//        }
//    }
//
//
//    //이미지 삭제 api
//    @DeleteMapping("/deleteImage")
//    public ResponseEntity<ResponseMessage> deleteImage(@RequestBody ImageDataDTO imageDataDTO){
//        try {
//            Long ImageId = imageDataDTO.getImageId();
//            imageService.deleteImage(ImageId);
//            ResponseMessage message = new ResponseMessage(200, "이미지 삭제 완료");
//            return new ResponseEntity<>(message, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            ResponseMessage message = new ResponseMessage(400,  e.getMessage());
//            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//        }
//    }
//
//
//    //이미지내에 설정한 위치들의 목록을 가져오는 api
//    @GetMapping("/getPositionList")
//    public ResponseEntity<ResponseMessage> getPositionList(){
//        try {
//        List<PositionDTO> positionList = imageService.getPositionList();
//        ResponseMessage message = new ResponseMessage(200, "position 목록 조회 완료",positionList);
//        return new ResponseEntity<>(message, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            ResponseMessage message = new ResponseMessage(400,  e.getMessage());
//            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//        }
//
//    }
//
//
//    //이미지내에 설정한 위치들과 해당 좌표 목록을 가져오는 api
//    @GetMapping("/getPositionAndCoordinateList")
//    public ResponseEntity<ResponseMessage> getPositionAndCoordinateList(){
//        try {
//            List<CoordinateDTO> positionList = imageService.getPositionAndCoordinateList();
//            ResponseMessage message = new ResponseMessage(200, "position 및 좌표 조회 완료",positionList);
//            return new ResponseEntity<>(message, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            ResponseMessage message = new ResponseMessage(400,  e.getMessage());
//            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//        }
//    }
//
//
//    //이미지 내 위치 및 범위 생성 api
//    @PostMapping("/postImagePositionAndCoordinates")
//    public ResponseEntity<ResponseMessage> saveImagePositionAndCoordinates(@RequestBody LabelDataDTO labelDataDTO){
//        try{
//            imageService.saveImagePositionAndCoordinates(labelDataDTO);
//            ResponseMessage message = new ResponseMessage(200, "이미지 내 위치 및 범위 생성 완료");
//            return new ResponseEntity<>(message, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            ResponseMessage message = new ResponseMessage(400,  e.getMessage());
//            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//        }
//
//    }
//
//    //이미지 내 위치 및 범위 삭제 api
//    @DeleteMapping("/deleteImagePositionAndCoordinates/{coordinateId}")
//    public ResponseEntity<ResponseMessage> deleteImagePositionAndCoordinates(@PathVariable Long coordinateId) {
//        try{
//            imageService.deleteImagePositionAndCoordinates(coordinateId);
//            ResponseMessage message = new ResponseMessage(200, "이미지 내 위치 및 범위 삭제 완료");
//            return new ResponseEntity<>(message, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            ResponseMessage message = new ResponseMessage(400,  e.getMessage());
//            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//        }
//    }
//}
//
