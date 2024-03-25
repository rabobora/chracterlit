package com.vamos.characterlit.pay.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindUserkeyResponseDTO {
    private String userId;
    private String username;
    private String institutionCode;
    private String userKey;
    private String created;
    private String modified;

}
