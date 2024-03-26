package com.vamos.characterlit.pay.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    private String paymentId;
    private String pgTid;
    private Long userNumber;
    private int money;
    @Setter
    private LocalDateTime paymentDate;
    @Setter
    private int paymentStatus;
}
