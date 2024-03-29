package com.vamos.characterlit.sse.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisconnectDTO {
    private Long bidId;
    private Long sessionId;
}
