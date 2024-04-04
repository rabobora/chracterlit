package com.vamos.characterlit.pay.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.vamos.characterlit.pay.domain.Payment;
import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.repository.PaymentRepository;
import com.vamos.characterlit.pay.repository.PointRepository;
import com.vamos.characterlit.pay.response.KaKaoApproveResponseDTO;
import com.vamos.characterlit.pay.response.KakaoReadyResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final PointStatementService pointStatementService;
    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;
    private final PointService pointService;

    @Value("${spring.pay.cid}")
    private String cid;

    @Value("${spring.pay.secretKey}")
    private String secretKey;

    @Value("${spring.pay.readyUrl}")
    private String readyUrl;

    @Value("${spring.pay.approveUrl}")
    private String approveUrl;

    @Value("${spring.pay.host}")
    private String host;

    // 결제 준비
    public KakaoReadyResponseDTO kakaoReady(int money, Long userNumber) {

        LocalDateTime now = LocalDateTime.now();
        String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String orderId = transmissionDate + pointStatementService.createOrderId();

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id",userNumber.toString());
        params.put("item_name", "Characterlit Point");
        params.put("quantity", "1");
        params.put("total_amount", String.valueOf(money));
        params.put("tax_free_amount", String.valueOf(money));
        params.put("approval_url", host+"/loading?order_id=" + orderId);
        params.put("cancel_url", host+"/mypage");
        params.put("fail_url", host+"/mypage");
        params.put("payment_method_type", "MONEY");

        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + secretKey;
        headers.add("Content-type","application/json");
        headers.add("Authorization",auth);

        // 헤더와 바디 붙이기
        HttpEntity<Map<String,Object>> body = new HttpEntity<>(params, headers);

        KakaoReadyResponseDTO kakaoResponse = null;
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        try {
            kakaoResponse = restTemplate.postForObject(readyUrl, body, KakaoReadyResponseDTO.class);
        }catch (RestClientException e) {
            System.out.println(e);
        }

        LocalDateTime localDateTime = kakaoResponse.getCreated_at().toInstant() // Date -> Instant
                .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                .toLocalDateTime(); // ZonedDateTime -> LocalDateTime

        Payment payment = Payment.builder()
                .paymentId(orderId)
                .pgTid(kakaoResponse.getTid())
                .userNumber(userNumber)
                .money(money)
                .paymentDate(localDateTime)
                .paymentStatus(0)
                .build();

        paymentRepository.save(payment);

        return kakaoResponse;
    }

    // 결제 승인
    public void kakaoApprove(String pgToken, String orderId) {

        Payment payment = paymentRepository.findByPaymentId(orderId);

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", payment.getPgTid());
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", String.valueOf(payment.getUserNumber()));
        params.put("pg_token", pgToken);
//        params.put("total_amount", String.valueOf(payment.getMoney()));

        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + secretKey;
        headers.add("Content-type","application/json");
        headers.add("Authorization",auth);

        // 헤더와 바디 붙이기
        HttpEntity<Map<String,Object>> body = new HttpEntity<>(params, headers);

        KaKaoApproveResponseDTO kakaoResponse = null;
        RestTemplate restTemplate = new RestTemplate();

        try {
            kakaoResponse = restTemplate.postForObject(approveUrl, body, KaKaoApproveResponseDTO.class);

        }catch (RestClientException e) {
            System.out.println(e);
        }

        LocalDateTime localDateTime = kakaoResponse.getApproved_at().toInstant() // Date -> Instant
                .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                .toLocalDateTime(); // ZonedDateTime -> LocalDateTime

        payment.setPaymentStatus(1);
        payment.setPaymentDate(localDateTime);
        paymentRepository.save(payment);

        Point point = pointService.getPoint(payment.getUserNumber());
        Point updatePoint = Point.builder()
                .userNumber(payment.getUserNumber())
                .allPoint(point.getAllPoint() + (payment.getMoney()-(int)(payment.getMoney()*0.005)))
                .usablePoint(point.getUsablePoint() + (payment.getMoney()-(int)(payment.getMoney()*0.005)))
                .build();

        pointRepository.save(updatePoint);
    }

}

