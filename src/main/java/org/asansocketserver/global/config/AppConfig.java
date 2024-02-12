package org.asansocketserver.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AppConfig {

    //비콘 수집 상태에 있는 워치 id를 저장하는 HashMap ex) {"id1" : "216호"} -> 지금 id1으로 들어오는 비콘을 216호로 라벨링해서 저장
    @Bean
    public ConcurrentHashMap<String, String> stateHashMap() {
        return new ConcurrentHashMap<>();
    }

    // 비콘 위치 추적 결과를 나타내는 HashMap ex) {"id1":"216호,"id2: "217호"}
    @Bean
    public ConcurrentHashMap<String, String> resultHashMap() {
        return new ConcurrentHashMap<>();
    }
}
