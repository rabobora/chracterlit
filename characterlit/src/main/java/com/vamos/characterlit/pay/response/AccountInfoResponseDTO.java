package com.vamos.characterlit.pay.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountInfoResponseDTO {
    private String bankCode;
    private String bankName;
    private String username;
    private String accountNo;
    private String accountName;
    private String accountTypeCode;
    private String accountTypeName;
    private String accountCreatedDate;
    private String accountExpiryDate;
    private Long dailyTransferLimit;
    private Long oneTimeTransferLimit;
    private Long accountBalance;
    private String lastTransactionDate;
}
