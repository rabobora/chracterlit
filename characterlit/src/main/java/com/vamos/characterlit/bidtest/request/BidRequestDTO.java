package com.vamos.characterlit.bidtest.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidRequestDTO {

    private Long bidId;
    private Long userId;
    private int requestBid;
    private LocalDateTime bidTime;


}
