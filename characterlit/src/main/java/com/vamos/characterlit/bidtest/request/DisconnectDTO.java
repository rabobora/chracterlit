package com.vamos.characterlit.bidtest.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisconnectDTO {
    private Long bidId;
    private Long userId;
    private String nickname;
}
