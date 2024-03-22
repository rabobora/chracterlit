package com.vamos.characterlit.bid.controller;

import com.vamos.characterlit.bid.request.BidRequestDTO;
import com.vamos.characterlit.bid.service.NowbidService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bid")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BidController {

    private final NowbidService nowbidService;

    public BidController(NowbidService nowbidService) {
        this.nowbidService = nowbidService;
    }

    @PostMapping("/read/{bidId}")
    public ResponseEntity<?> bid(@PathVariable("bidId") Long bidId,
                                 @RequestBody BidRequestDTO bidRequestDTO,
                                 HttpSession session) {
        log.info("Received bid request for bidId: {}", bidId);

        try {
            // 경매 정보를 업데이트하는 비즈니스 로직 호출
            nowbidService.update(bidId, bidRequestDTO);

            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error processing the bid: {}", e.getMessage());
            return new ResponseEntity<>("Error processing the bid: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
