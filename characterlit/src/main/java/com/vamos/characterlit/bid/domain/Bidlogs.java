package com.vamos.characterlit.bid.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Bidlogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    private Long bidId;
    private Long userId;
    private int requestBid;
    private LocalDateTime bidTime;

    public Bidlogs() {}
    @Builder
    public Bidlogs(Long logId, Long bidId, Long userId, int requestBid, LocalDateTime bidTime) {
        this.logId = logId;
        this.bidId = bidId;
        this.userId = userId;
        this.requestBid = requestBid;
        this.bidTime = bidTime;
    }
    // 메시지를 보내는 시점에서 시간이 기록되어야 합니다. 사용 안할것.
//    @PrePersist
//    protected void onCreate() {
//        bidTime = LocalDateTime.now();
//    }
}
