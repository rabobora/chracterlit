package com.vamos.characterlit.bid.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyLogDTO {
    private Long bidId;
    private Long userNumber;
    private int requestBid;

    public MyLogDTO(Long bidId, Long userNumber, int requestBid) {
        this.bidId = bidId;
        this.userNumber = userNumber;
        this.requestBid = requestBid;
    }
}
