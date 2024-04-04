package com.vamos.characterlit.bid.controller;

import com.vamos.characterlit.auth2.annotation.ExtractPayload;
import com.vamos.characterlit.auth2.security.jwt.JWTUtil;
import com.vamos.characterlit.bid.repository.NowbidRepository;
import com.vamos.characterlit.bid.request.BidMessageDTO;
import com.vamos.characterlit.bid.request.BidRequestDTO;
import com.vamos.characterlit.bid.response.MyBidList;
import com.vamos.characterlit.bid.response.MySellListDTO;
import com.vamos.characterlit.bid.service.MyBidService;
import com.vamos.characterlit.bid.service.NowbidService;
import com.vamos.characterlit.items.domain.Items;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bid")
@Slf4j
@CrossOrigin(origins = "http://j10b105.p.ssafy.io:5173", allowCredentials = "true")
public class BidController {

    private final RabbitTemplate template;
    private final DirectExchange direct;
    private final NowbidService nowbidService;
    private final MyBidService myBidService;
    private final JWTUtil jwtUtil;

    public BidController(RabbitTemplate template, DirectExchange direct, NowbidService nowbidService, MyBidService myBidService, JWTUtil jwtUtil) {
        this.template = template;
        this.direct = direct;
        this.nowbidService = nowbidService;
        this.myBidService = myBidService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/read/{bidId}")
    public ResponseEntity<?> bid(@PathVariable("bidId") Long bidId,
                                 @RequestBody BidRequestDTO bidRequestDTO,
                                 @ExtractPayload Long userNumber) {

        log.info("API Received bid request for bidId: {}, userNumber: {}", bidId, userNumber);

        try {
            // rabbitMQ에게 메시지 전달
            BidMessageDTO bidMessage = BidMessageDTO.builder()
                    .bidId(bidId)
                    .sessionId(bidRequestDTO.getSessionId())
                    .userNumber(userNumber)
                    .requestBid(bidRequestDTO.getRequestBid())
                    .bidTime(LocalDateTime.now())
                    .build();
            if (bidId % 2 == 0) {
                template.convertAndSend(direct.getName(), "queue_1_routing_key", bidMessage);
            } else {
                template.convertAndSend(direct.getName(), "queue_2_routing_key", bidMessage);
            }

            return new ResponseEntity<>("Message Add", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error processing the bid: {}", e.getMessage());
            return new ResponseEntity<>("Error processing the bid: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/read/now/{bidId}")
    public ResponseEntity<?> now(@PathVariable("bidId") Long bidId) {
        try {
            int presentBid = nowbidService.readPrice(bidId);
            log.info("Page init, send present bid price : {}", presentBid);
            return new ResponseEntity<Integer>(presentBid, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error call : {}", e.getMessage());
            return new ResponseEntity<>("Error processing the bid: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mybid")
    public ResponseEntity<?> mybid(@ExtractPayload Long userNumber) {
        try {
            log.info("read my bidlist userNumber: {}", userNumber);
            MyBidList biddingList = myBidService.show(userNumber);
            return new ResponseEntity<MyBidList>(biddingList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error call : {}", e.getMessage());
            return new ResponseEntity<>("Error in mybid list: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mysell")
    public ResponseEntity<?> mysell(@ExtractPayload Long userNumber) {
        try {
            log.info("read my sell");
            List<MySellListDTO> mySellList = myBidService.mysell(userNumber);
            return new ResponseEntity<List<MySellListDTO>>(mySellList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error in my sell: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
