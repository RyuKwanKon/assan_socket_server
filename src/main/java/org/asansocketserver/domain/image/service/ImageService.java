package org.asansocketserver.domain.image.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.image.dto.CoordinateDTO;
import org.asansocketserver.domain.image.dto.LabelDataDTO;
import org.asansocketserver.domain.position.dto.PositionDTO;
import org.asansocketserver.domain.image.entity.Coordinate;
import org.asansocketserver.domain.image.entity.Image;
import org.asansocketserver.domain.image.repository.CoordinateRepository;
import org.asansocketserver.domain.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final CoordinateRepository coordinateRepository;


    @Transactional
    public String getImage(){
        List<Image> image = imageRepository.findAll();
        return image.get(0).getImageUrl();
    }


    @Transactional
    public Long saveImage(String imageUrl) {
        //유저 가져오는 부분까지 해야함.
        Image image = Image.builder().imageUrl(imageUrl).build();

        List<Image> imageAll = imageRepository.findAll();
        int imageCounts = imageAll.size();

        if (imageCounts > 0 )  {
            throw new IllegalArgumentException("이미지가 이미 업로드되어 있습니다. 이전 이미지를 삭제해주세요.");
        }

        Image saveImage = imageRepository.save(image);
        return saveImage.getId();
    }


    @Transactional
    public void deleteImage(Long imageId) {

        Image image = (imageRepository.findById(imageId).orElseThrow(() ->
                new IllegalArgumentException("해당 이미지가 존재하지 않습니다 :" + imageId)));

        imageRepository.delete(image);
    }

    @Transactional
    public void saveImagePositionAndCoordinates(LabelDataDTO labelDataDTO) {

        Image image = (imageRepository.findById(labelDataDTO.getImageId()).orElseThrow(() ->
                new IllegalArgumentException("해당 이미지가 존재하지 않습니다")));


        Coordinate existingCoordinate = coordinateRepository.findByImageIdAndPosition(image, labelDataDTO.getPosition());

        if (existingCoordinate != null) {
            throw new IllegalArgumentException("해당 이미지의 위치가 이미 존재합니다.");
        }

        try {
            Coordinate coordinate = Coordinate.builder()
                    .imageId(image)
                    .position(labelDataDTO.getPosition())
                    .startX(labelDataDTO.getStartX())
                    .startY(labelDataDTO.getStartY())
                    .endX(labelDataDTO.getEndX())
                    .endY(labelDataDTO.getEndY()).build();

            coordinateRepository.save(coordinate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public void deleteImagePositionAndCoordinates(Long coordinateId) {


        Coordinate coordinate = (coordinateRepository.findById(coordinateId).orElseThrow(() ->
                new IllegalArgumentException("해당 위치 및 좌표가 존재하지 않습니다 : unknown id = " + coordinateId)));

        coordinateRepository.delete(coordinate);
    }

    @Transactional
    public List<CoordinateDTO> getPositionAndCoordinateList(){
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        List<CoordinateDTO> coordinateDTOList = new ArrayList<>();

        if (coordinateList.isEmpty()) {
            throw new IllegalArgumentException("해당 이미지의 위치 목록이 존재하지 않습니다");
        }
        else{

            for (Coordinate coordinate : coordinateList) {
                CoordinateDTO coordinateDTO = new CoordinateDTO();
                coordinateDTO.setImageId(coordinate.getImageId().getId());
                coordinateDTO.setCoordinateId(coordinate.getId());
                coordinateDTO.setPosition(coordinate.getPosition());
                coordinateDTO.setStartX(coordinate.getStartX());
                coordinateDTO.setStartY(coordinate.getStartY());
                coordinateDTO.setEndX(coordinate.getEndX());
                coordinateDTO.setEndY(coordinate.getEndX());

                coordinateDTOList.add(coordinateDTO);
            }
        }

        return coordinateDTOList;
    }

    @Transactional
    public List<PositionDTO> getPositionList(){
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        List<PositionDTO> positionList = new ArrayList<>();

        if (coordinateList.isEmpty()) {
            throw new IllegalArgumentException("해당 이미지의 위치 목록이 존재하지 않습니다");
        }
        else{
            for (Coordinate coordinate : coordinateList) {
                PositionDTO positionDTO = new PositionDTO();
                positionDTO.setImageId(coordinate.getImageId().getId());
                positionDTO.setCoordinateId(coordinate.getId());
                positionDTO.setPosition(coordinate.getPosition());

                positionList.add(positionDTO);
            }
        }
        return positionList;
    }
}



