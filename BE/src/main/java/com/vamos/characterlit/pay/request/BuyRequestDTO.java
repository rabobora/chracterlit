package com.vamos.characterlit.pay.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
public class BuyRequestDTO {
    private Long bidId;
    private Long userNumber;
    private int finalBid;
    private Long winnerNumber;
}
