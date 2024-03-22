package com.vamos.characterlit.pay.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class StatementResponseDTO {
    private Long statementId;
    private int point;
    private LocalDateTime statementDate;
    private int pointStatus;
}
