package org.asansocketserver.domain.watch.entity;

import jakarta.persistence.*;
import lombok.*;

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

    public static Watch createWatch(String uuid) {
        return Watch.builder()
                .uuid(uuid)
                .build();
    }
}
