package com.vamos.characterlit.pay.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BuyRequestDTO {
    private Long bidId;
    private Long userNumber;
    private int finalBid;
    private Long winnerId;
}
