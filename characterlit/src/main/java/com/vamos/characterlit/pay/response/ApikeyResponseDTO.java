package com.vamos.characterlit.pay.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApikeyResponseDTO {

    private String managerId;
    private String apiKey;
}
