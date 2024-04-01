package com.vamos.characterlit.bid.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidRequestDTO {

    private Long userId;
    private Long sessionId;
    private int requestBid;
    private LocalDateTime bidTime;


}
