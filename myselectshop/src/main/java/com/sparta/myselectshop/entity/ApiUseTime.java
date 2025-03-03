package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "api_use_time")
public class ApiUseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne // 한유저의 시간을 기록 // 단반향 유저는 시간을 확인할 일이 없기때문
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long totalTime;

    public ApiUseTime(User user, Long totalTime) {
        this.user = user;
        this.totalTime = totalTime;
    }

    public void addUseTime(long useTime) { // 사용 시간 누적
        this.totalTime += useTime;
    }
}