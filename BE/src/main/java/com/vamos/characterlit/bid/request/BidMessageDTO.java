package com.vamos.characterlit.bid.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidMessageDTO {
    private Long bidId;
    private Long sessionId;
    private Long userNumber;
    private int requestBid;
    private LocalDateTime bidTime;


}
