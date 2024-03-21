package com.vamos.characterlit.pay.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountHistoryResponseDTO {
    private int transactionUniqueNo;
    private String transactionDate;
    private String transactionTime;
    private String transactionType;
    private String transactionTypeName;
    private int transactionBalance;
    private int transactionAfterBalance;
    private String transactionSummary;
}
