package com.vamos.characterlit.bid.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyNowbidDTO {
    private Long bidId;
    private int presentBid;

    public MyNowbidDTO(Long bidId, int presentBid) {
        this.bidId = bidId;
        this.presentBid = presentBid;
    }
}
