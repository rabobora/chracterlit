package com.vamos.characterlit.pay.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountTransferRequestDTO {

    private String depositBankCode;
    private String depositAccountNo;
    private int transactionBalance;
    private String withdrawalBankCode;
    private String withdrawalAccountNo;
    private String depositTransactionSummary;
    private String withdrawalTransactionSummary;
}