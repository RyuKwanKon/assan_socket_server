package org.asansocketserver.domain.watch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.asansocketserver.domain.watch.dto.request.WatchUpdateRequestDto;
import org.asansocketserver.domain.watch.dto.web.request.WatchUpdateRequestForWebDto;
import org.asansocketserver.domain.watch.enums.Gender;
import org.asansocketserver.domain.watch.enums.HighRisk;

import java.util.ArrayList;
import java.util.List;

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
    private int minHR = 60;
    private int maxHR = 130;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private HighRisk highRisk;

    @ElementCollection
    @CollectionTable(name = "accessible_area", joinColumns = @JoinColumn(name = "watch_id"))
    @Column(name = "area_id")
    private List<Long> accessibleAreaList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "no_contact_patient", joinColumns = @JoinColumn(name = "watch_id"))
    @Column(name = "patient_id")
    private List<Long> noContactPatientList = new ArrayList<>();


    public static Watch createWatch(String uuid, String device) {
        return Watch.builder()
                .uuid(uuid)
                .device(device)
                .name("지정되지않음")
                .host("지정되지않음")
                .minHR(60)
                .maxHR(130)
                .gender(Gender.M)
                .highRisk(HighRisk.없음)
                .accessibleAreaList(new ArrayList<>())
                .noContactPatientList(new ArrayList<>())
                .build();
    }

    public void updateWatch(WatchUpdateRequestDto requestDto) {
        this.name = requestDto.name();
        this.host = requestDto.host();
    }

    public void updateWatchForWeb(WatchUpdateRequestForWebDto requestDto) {
        this.name = requestDto.name();
        this.host = requestDto.host();
        this.gender = Gender.valueOf(requestDto.gender());
        this.highRisk = HighRisk.valueOf(requestDto.highrisk());
        this.accessibleAreaList = requestDto.accessibleAreaList();
        this.noContactPatientList = requestDto.noContactPatientList();
    }
}
