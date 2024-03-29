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
    private Long userNumber;
    private int requestBid;
    private LocalDateTime bidTime;

    public Bidlogs() {}
    @Builder
    public Bidlogs(Long logId, Long bidId, Long userNumber, int requestBid, LocalDateTime bidTime) {
        this.logId = logId;
        this.bidId = bidId;
        this.userNumber = userNumber;
        this.requestBid = requestBid;
        this.bidTime = bidTime;
    }
}
