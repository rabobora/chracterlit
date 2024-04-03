package com.vamos.characterlit.pay.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChargeRequestDTO {
    private String bankCode;
    private String accountNo;
    private int transactionBalance;
}
