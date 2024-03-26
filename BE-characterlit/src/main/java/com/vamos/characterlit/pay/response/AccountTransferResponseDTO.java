package com.vamos.characterlit.pay.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
public class AccountTransferResponseDTO {
    private int transactionUniqueNo;
    private String accountNo;
    private String transactionDate;
    private String transactionType;
    private String transactionTypeName;
    private String transactionAccountNo;
}
