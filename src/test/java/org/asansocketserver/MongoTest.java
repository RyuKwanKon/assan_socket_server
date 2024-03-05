//package org.asansocketserver;
//
//import org.asansocketserver.domain.sensor.dto.request.HeartRateRequestDto;
//import org.asansocketserver.domain.sensor.entity.SensorHeartRate;
//import org.asansocketserver.domain.sensor.entity.sensorType.HeartRate;
//import org.asansocketserver.domain.sensor.mongorepository.heartrate.SensorHeartRateRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@DataMongoTest
//public class MongoTest {
//    @Autowired
//    private SensorHeartRateRepository sensorHeartRateRepository;
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Test
//    void create() {
//        // given
//        SensorHeartRate sensorHeartRate = SensorHeartRate.createSensor(1L);
//        // when
//        SensorHeartRate actual = sensorHeartRateRepository.save(sensorHeartRate);
//        // then
//        assertThat(actual).isNotNull();
//    }
//
//    @Test
//    void update() {
//        // given
//        Long watchId = 1L;
//        LocalDate currentDate = LocalDate.now();
//        HeartRateRequestDto heartRateRequestDto = new HeartRateRequestDto(80, "124");
//        HeartRate heartRate = HeartRate.createHeartRate(heartRateRequestDto);
//        //when
//        Query query = new Query();
//        Update update = new Update();
//        query.addCriteria(Criteria.where("date").is(currentDate)
//                .and("watchId").is(watchId));
//        update.addToSet("heartRateList", heartRate);
//        mongoTemplate.updateFirst(query, update, SensorHeartRate.class);
//        // then
//        Optional<SensorHeartRate> actual = sensorHeartRateRepository.findOneByWatchIdAndDate(watchId, currentDate);
//        assertEquals(5, actual.get().getHeartRateList().size());
//    }
//}
