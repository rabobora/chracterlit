package com.vamos.characterlit.pay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.vamos.characterlit.pay.response.FindUserkeyResponseDTO;
import com.vamos.characterlit.pay.response.KakaoReadyResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    @Value("${spring.pay.cid}")
    private String cid;

    @Value("${spring.pay.secretKey}")
    private String secretKey;

    @Value("${spring.pay.readyUrl}")
    private String readyUrl;

    // 1. 결제 준비 API
    public KakaoReadyResponseDTO kakaoReday(int money, Long userId) {

        LocalDateTime now = LocalDateTime.now();
        String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String orderId = transmissionDate + transmissionTime + randomNumber;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", cid);
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", String.valueOf(userId));
        params.add("item_name", "Characterlit Point");
        params.add("quantity", "1");
        params.add("total_amount", String.valueOf(money));
        params.add("tax_free_amount", String.valueOf(money));
        params.add("approval_url", "성공주소");
        params.add("cancel_url", "실패주소");
        params.add("fail_url", "취소주소");
        params.add("payment_method_type", "MONEY");

        WebClient wc = WebClient.create(readyUrl);
        String response = wc.post()
                .uri(readyUrl)
                .body(BodyInserters.fromFormData(params))
                .header("Authorization", "KakaoAK " + secretKey)
                .header("Content-type", "application/json") //요청 헤더
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        Gson gson = new Gson();
        KakaoReadyResponseDTO kakaoResponse = null;

        try {
            kakaoResponse = gson.fromJson(jsonObject, KakaoReadyResponseDTO.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return kakaoResponse;
    }
    // 2. 결제 요청 API
    // 3. 결제 승인 API

}
