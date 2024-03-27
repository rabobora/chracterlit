package com.vamos.characterlit.bid.controller;

import com.vamos.characterlit.auth2.annotation.ExtractPayload;
import com.vamos.characterlit.bid.repository.NowbidRepository;
import com.vamos.characterlit.bid.request.BidMessageDTO;
import com.vamos.characterlit.bid.request.BidRequestDTO;
import com.vamos.characterlit.bid.service.NowbidService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bid")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BidController {

    private final RabbitTemplate template;
    private final DirectExchange direct;
    private final NowbidService nowbidService;

    public BidController(RabbitTemplate template, DirectExchange direct, NowbidService nowbidService) {
        this.template = template;
        this.direct = direct;
        this.nowbidService = nowbidService;
    }

    @PostMapping("/read/{bidId}")
    public ResponseEntity<?> bid(@PathVariable("bidId") Long bidId,
                                 @RequestBody BidRequestDTO bidRequestDTO,
                                 @ExtractPayload String userId,
                                 HttpSession session) {
        log.info("API Received bid request for bidId: {}", bidId);

        try {
            // rabbitMQ에게 메시지 전달
            BidMessageDTO bidMessage = BidMessageDTO.builder()
                    .bidId(bidId)
                    .userId(bidRequestDTO.getUserId())
                    .requestBid(bidRequestDTO.getRequestBid())
                    .bidTime(LocalDateTime.now())
                    .build();
            if(bidId%2==0){
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
    public ResponseEntity<?> now(@PathVariable("bidId") Long bidId){
        try {
            int presentBid = nowbidService.readPrice(bidId);
            log.info("Page init, send present bid price : {}", presentBid);
            return new ResponseEntity<Integer>(presentBid, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error call : {}", e.getMessage());
            return new ResponseEntity<>("Error processing the bid: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
