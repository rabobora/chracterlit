package com.vamos.characterlit.bidtest.controller;

import com.vamos.characterlit.bidtest.request.BidRequestDTO;
import com.vamos.characterlit.bidtest.service.BidlogsService;
import com.vamos.characterlit.bidtest.service.NowbidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/bid")
@Slf4j
@CrossOrigin("*")
public class BidController {
    private final BidlogsService bidlogsService;
    private final NowbidService nowbidService;

    public BidController(BidlogsService bidlogsService, NowbidService nowbidService) {
        this.bidlogsService = bidlogsService;
        this.nowbidService = nowbidService;
    }

    @PostMapping("/read/{bidId}")
    public ResponseEntity<?>bid( //@RequestHeader("Authorization") String token,
                                    @RequestBody BidRequestDTO bidRequestDTO){
        try {
            // RabbitMQ를 통한 메시지 수신
            // 입찰 상태 확인. 날짜는 경메 기간 내인가? 입찰가는 현재 입찰가 초과인가?
            // 입찰 내역 확인. 혹시 최고 낙찰자가 나인데 또다시 입찰을 보냈는가?
            // 임시코드
            // 전부 통과 시
            nowbidService.update(bidRequestDTO);

            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println("Error during bid process: " + e.getMessage());
            // 클라이언트에게 에러 메시지 반환
            return new ResponseEntity<>("Error processing the bid: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
