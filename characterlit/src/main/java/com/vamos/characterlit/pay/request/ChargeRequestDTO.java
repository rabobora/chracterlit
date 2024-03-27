package com.vamos.characterlit.pay.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChargeRequestDTO {
    private int bankCode;
    private int accountNo;
    private int transactionBalance;
}
