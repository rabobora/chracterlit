package com.vamos.characterlit.pay.controller;


import com.vamos.characterlit.auth2.annotation.ExtractPayload;
import com.vamos.characterlit.pay.domain.Point;
import com.vamos.characterlit.pay.request.BuyRequestDTO;
import com.vamos.characterlit.pay.request.ChargeRequestDTO;
import com.vamos.characterlit.pay.response.KakaoReadyResponseDTO;
import com.vamos.characterlit.pay.response.PointResponseDTO;
import com.vamos.characterlit.pay.service.KakaoPayService;
import com.vamos.characterlit.pay.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {

    private final PointService pointService;
    private final KakaoPayService kakaoPayService;

    // 포인트 조회
    @GetMapping
    public ResponseEntity<PointResponseDTO> pointCheck(@ExtractPayload Long userNumber){

        Point point = pointService.getPoint(userNumber);

        return new ResponseEntity<>(
                PointResponseDTO.builder()
                        .allPoint(point.getAllPoint())
                        .usablePoint(point.getUsablePoint())
                        .build()
                , HttpStatus.OK);
    }

//    // 사용가능한 포인트 조회
//    @GetMapping("/usable")
//    public ResponseEntity<Integer> usablePointCheck(@ExtractPayload Long userNumber){
//        Point point = pointService.getPoint(userNumber);
//        return new ResponseEntity<>(point.getUsablePoint(),HttpStatus.OK);
//    }

    // 계좌이체 포인트 충전 결제
    @PutMapping("/charge")
    public ResponseEntity<Void> pointCharge(@ExtractPayload Long userNumber, @RequestBody ChargeRequestDTO request){
        pointService.charge(request,userNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 카카오페이 포인트 충전 결제 준비
    @PostMapping("/charge/kakao/ready")
    public ResponseEntity<KakaoReadyResponseDTO> kakaopayReady(@ExtractPayload Long userNumber, @RequestParam int money){

        KakaoReadyResponseDTO kakaoReadyResponseDTO = kakaoPayService.kakaoReady(money, userNumber);

        return new ResponseEntity<>(kakaoReadyResponseDTO,HttpStatus.OK);
    }

    // 카카오페이 포인트 충전 결제 승인
    @GetMapping("/charge/kakao/approve")
    public ResponseEntity<Void> kakaopayApprove(@RequestParam("pg_token") String pgToken,@RequestParam("order_id") String orderId){

        kakaoPayService.kakaoApprove(pgToken,orderId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 포인트 출금
    @PutMapping("/withdraw")
    public ResponseEntity<Void> pointWithdraw(@ExtractPayload Long userNumber, @RequestBody ChargeRequestDTO request){
        pointService.withdraw(request,userNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 구매하기
    @PutMapping("/buy")
    public ResponseEntity<Void> buyItem(@RequestBody BuyRequestDTO request){

        boolean check = pointService.buyItem(request);

        if(check=false){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}