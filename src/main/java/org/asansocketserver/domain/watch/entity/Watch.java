package org.asansocketserver.domain.watch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.asansocketserver.domain.watch.dto.request.WatchUpdateRequestDto;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "watch")
@Entity
public class Watch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "watch_id")
    private Long id;
    private String uuid;
    private String device;
    private String name;
    private String host;

    public static Watch createWatch(String uuid, String device) {
        return Watch.builder()
                .uuid(uuid)
                .device(device)
                .name("지정되지않음")
                .host("지정되지않음")
                .build();
    }

    public void updateWatch(WatchUpdateRequestDto requestDto) {
        this.name = requestDto.name();
        this.host = requestDto.host();
    }
}
