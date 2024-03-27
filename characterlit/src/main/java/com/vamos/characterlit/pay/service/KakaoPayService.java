package com.vamos.characterlit.pay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.vamos.characterlit.pay.domain.Payment;
import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.repository.PaymentRepository;
import com.vamos.characterlit.pay.repository.PointRepository;
import com.vamos.characterlit.pay.response.FindUserkeyResponseDTO;
import com.vamos.characterlit.pay.response.KaKaoApproveResponseDTO;
import com.vamos.characterlit.pay.response.KakaoReadyResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final PointStatementService pointStatementService;
    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;

    @Value("${spring.pay.cid}")
    private String cid;

    @Value("${spring.pay.secretKey}")
    private String secretKey;

    @Value("${spring.pay.readyUrl}")
    private String readyUrl;

    @Value("${spring.pay.approveUrl}")
    private String approveUrl;

    // 결제 준비
    public KakaoReadyResponseDTO kakaoReady(int money, Long userNumber) {

        LocalDateTime now = LocalDateTime.now();
        String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String orderId = transmissionDate +pointStatementService.createOrderId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", cid);
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", String.valueOf(userNumber));
        params.add("item_name", "Characterlit Point");
        params.add("quantity", "1");
        params.add("total_amount", String.valueOf(money));
        params.add("tax_free_amount", String.valueOf(money));
        params.add("approval_url", "http://localhost:8080/api/point/charge/kakao/success?order_id="+orderId);
        params.add("cancel_url", "http://localhost:8080/api/point/charge/kakao/cancel");
        params.add("fail_url", "http://localhost:8080/api/point/charge/kakao/fail");
        params.add("payment_method_type", "MONEY");

        WebClient wc = WebClient.create(readyUrl);
        KakaoReadyResponseDTO kakaoResponse = null;

        try {
            String response = wc.post()
                    .uri(readyUrl)
                    .body(BodyInserters.fromFormData(params))
                    .header("Authorization", "SECRET_KEY " + secretKey)
                    .header("Content-type", "application/json") //요청 헤더
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            Gson gson = new Gson();

            kakaoResponse = gson.fromJson(jsonObject, KakaoReadyResponseDTO.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
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
    public void kakaoApprove(String pgToken, String orderId){

        Payment payment = paymentRepository.findByPaymentId(orderId);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", cid);
        params.add("tid", payment.getPgTid());
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", String.valueOf(payment.getUserNumber()));
        params.add("pg_token", pgToken);
        params.add("total_amount", String.valueOf(payment.getMoney()));

        WebClient wc = WebClient.create(approveUrl);
        KaKaoApproveResponseDTO kakaoResponse = null;

        try {
            String response = wc.post()
                    .uri(approveUrl)
                    .body(BodyInserters.fromFormData(params))
                    .header("Authorization", "SECRET_KEY " + secretKey)
                    .header("Content-type", "application/json") //요청 헤더
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            Gson gson = new Gson();

            kakaoResponse = gson.fromJson(jsonObject, KaKaoApproveResponseDTO.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        LocalDateTime localDateTime = kakaoResponse.getApproved_at().toInstant() // Date -> Instant
                .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                .toLocalDateTime(); // ZonedDateTime -> LocalDateTime

        payment.setPaymentStatus(1);
        payment.setPaymentDate(localDateTime);
        paymentRepository.save(payment);

        Point point = pointRepository.findByuserNumber(payment.getUserNumber());
        Point updatePoint = Point.builder()
                .userNumber(payment.getUserNumber() )
                .allPoint(point.getAllPoint()+ payment.getMoney())
                .usablePoint(point.getUsablePoint()+ payment.getMoney())
                .build();

        pointRepository.save(updatePoint);
    }

}
