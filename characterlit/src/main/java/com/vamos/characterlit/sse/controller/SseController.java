package com.vamos.characterlit.sse.controller;

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
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SseController {
    private final SseEmitterService sseEmitterService;

    public SseController(SseEmitterService sseEmitterService) {
        this.sseEmitterService = sseEmitterService;
    }
    //응답 mime type 은 반드시 text/event-stream 이여야 한다.
    //클라이언트로부터 SSE subscription 을 수락한다.
    @GetMapping(path = "/subscribe/{bidid}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    ResponseEntity<SseEmitter> subscribe(@PathVariable("bidid") String bidid, @RequestParam("nickname") String nickname, HttpSession session) {
        System.out.println("Session ID: " + session.getId() + ", Nickname: " + nickname); // 세션 ID와 닉네임 로깅
        if(nickname == null){
            throw new RuntimeException("no ssession");
        }
        String sseId = "bidId" + bidid;
        SseEmitter emitter = sseEmitterService.subscribe(sseId, nickname);
        return ResponseEntity.ok(emitter);
    }
    // 페이지 벗어나 연결을 종료합니다.
    @PostMapping("/disconnect")
    public ResponseEntity<Void> disconnect(@RequestBody DisconnectDTO disconnectDTO){
        System.out.println("disconnect request :" + disconnectDTO);
        sseEmitterService.disconnect(disconnectDTO);
        return  ResponseEntity.ok().build();
    }

}
