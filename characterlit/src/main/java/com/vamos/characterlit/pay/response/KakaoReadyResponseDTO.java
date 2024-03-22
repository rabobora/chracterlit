package com.vamos.characterlit.pay.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class KakaoReadyResponseDTO {
    private String tid;
    private String next_redirect_pc_url;
    private Date created_at;
}