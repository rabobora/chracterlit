package com.vamos.characterlit.pay.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointStatements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statementId;

    private Long userNumber;
    private int point;
    private LocalDateTime statementDate;
    @Setter
    private int pointStatus; // 충전(1), 구매(2), 출금(3), 홀딩(4), 정산(5)
    private Long bidId;
}
