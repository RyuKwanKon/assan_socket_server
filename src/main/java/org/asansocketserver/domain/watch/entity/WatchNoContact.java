package org.asansocketserver.domain.watch.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "watch_no_contact")
@Entity
public class WatchNoContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "watch_id")
    private Watch watch;

    @ManyToOne
    @JoinColumn(name = "no_contact_watch_id")
    private Watch noContactWatch;

    public static WatchNoContact createNoContact(Watch watch, Watch noContactWatch) {
        return WatchNoContact.builder()
                .watch(watch)
                .noContactWatch(noContactWatch)
                .build();
    }
}
