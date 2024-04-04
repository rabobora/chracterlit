package com.vamos.characterlit.sse.controller;

import com.vamos.characterlit.auth2.annotation.ExtractPayload;
import com.vamos.characterlit.sse.request.DisconnectDTO;
import com.vamos.characterlit.sse.service.SseEmitterService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@Slf4j
@CrossOrigin(origins = "http://j10b105.p.ssafy.io:5173", allowCredentials = "true")
public class SseController {
    private final SseEmitterService sseEmitterService;

    public SseController(SseEmitterService sseEmitterService) {
        this.sseEmitterService = sseEmitterService;
    }
    //응답 mime type 은 반드시 text/event-stream 이여야 한다.
    @GetMapping(path = "/subscribe/{bidId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    ResponseEntity<SseEmitter> subscribe(@PathVariable("bidId") String bidId,
//                                         @ExtractPayload Long userNumber,
                                         @RequestParam(name = "sessionId", required = true) String sessionId) {
//        log.info("subscribe bidId:{}, userNumber: {}", bidId, userNumber); // 세션 ID와 닉네임 로깅
        Long userSession = Long.parseLong(sessionId);
        log.info("subscribe bidId:{}, userNumber: {}", bidId, userSession); // 세션 ID와 닉네임 로깅
//        if(userNumber == null){
//            throw new RuntimeException("SSE user auth failed");
//        }
//        if(userSession == null){
//            throw new RuntimeException("SSE user auth failed");
//        }
        String sseId = "bidId" + bidId;
//        SseEmitter emitter = sseEmitterService.subscribe(sseId, userNumber);
        SseEmitter emitter = sseEmitterService.subscribe(sseId, userSession);
        return ResponseEntity.ok(emitter);
    }
    // 페이지 벗어나 연결을 종료합니다.
    @PostMapping("/disconnect")
    public ResponseEntity<Void> disconnect(
//                                           @ExtractPayload Long userNumber,
                                           @RequestBody DisconnectDTO disconnectDTO){
        log.info("disconnect request :" + disconnectDTO);
//        disconnectDTO.setUserNumber(userNumber);
//        disconnectDTO.setUserNumber(userSession);
        sseEmitterService.disconnect(disconnectDTO);
        return  ResponseEntity.ok().build();
    }
}
