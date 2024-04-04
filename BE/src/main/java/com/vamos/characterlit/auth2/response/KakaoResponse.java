package com.vamos.characterlit.auth2.response;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    public String getEmail() {
        // "kakao_account" 키로 접근하여 해당 정보를 Map으로 변환
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");

        // "email" 키로 접근하여 이메일 값 가져오기
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getName() {

        return "unknown";
    }
}
