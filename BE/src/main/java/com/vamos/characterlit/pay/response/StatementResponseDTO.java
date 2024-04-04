package com.vamos.characterlit.pay.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class StatementResponseDTO {
    private Long statementId;
    private int point;
    private LocalDate statementDate;
    private int pointStatus;
}
