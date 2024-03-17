package com.vamos.characterlit.bidtest.controller;

import com.vamos.characterlit.bidtest.response.EventPayload;
import com.vamos.characterlit.bidtest.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@Slf4j
public class SseController {
    private final SseEmitterService sseEmitterService;

    public SseController(SseEmitterService sseEmitterService) {
        this.sseEmitterService = sseEmitterService;
    }
    //응답 mime type 은 반드시 text/event-stream 이여야 한다.
    //클라이언트로 부터 SSE subscription 을 수락한다.
    @GetMapping(path = "/v1/sse/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe() {
        String sseId = UUID.randomUUID().toString();
        SseEmitter emitter = sseEmitterService.subscribe(sseId);
        return ResponseEntity.ok(emitter);
    }

    //eventPayload 를 SSE 로 연결된 모든 클라이언트에게 broadcasting 한다.
    @PostMapping(path = "/v1/sse/broadcast")
    public ResponseEntity<Void> broadcast(@RequestBody EventPayload eventPayload) {
        sseEmitterService.broadcast(eventPayload);
        return ResponseEntity.ok().build();
    }
}
