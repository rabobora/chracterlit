package com.vamos.characterlit.bidtest.service;

import com.vamos.characterlit.bidtest.request.DisconnectDTO;
import com.vamos.characterlit.bidtest.response.EventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SseEmitterService {
    private final Map<String, List<SseEmitter>> emitterMap = new ConcurrentHashMap<>();
    private static final long TIMEOUT = 60 * 1000;
    private static final long RECONNECTION_TIMEOUT = 1000L;

    //    public SseEmitter subscribe(String bidId) {
//        SseEmitter emitter = createEmitter();
//        //연결 세션 timeout 이벤트 핸들러 등록
//        emitter.onTimeout(() -> {
//            log.info("server sent event timed out : id={}", bidId);
//            //onCompletion 핸들러 호출
//            emitter.complete();
//        });
//
//        //에러 핸들러 등록
//        emitter.onError(e -> {
//            log.info("server sent event error occurred : id={}, message={}", bidId, e.getMessage());
//            //onCompletion 핸들러 호출
//            emitter.complete();
//        });
//
//        //SSE complete 핸들러 등록
//        emitter.onCompletion(() -> {
//            //맵에서 핸들러 호출된 emitter 삭제
//            if (emitterMap.remove(id) != null) {
//                log.info("server sent event removed in emitter cache: id={}", bidId);
//            }
//
//            log.info("disconnected by completed server sent event: id={}", bidId);
//        });
//
//        emitterMap.put(id, emitter);
//
//        //초기 연결시에 응답 데이터를 전송할 수도 있다.
//        try {
//            SseEmitter.SseEventBuilder event = SseEmitter.event()
//                    //event 명 (event: event example)
//                    .name("event example")
//                    //event id (id: id-1) - 재연결시 클라이언트에서 `Last-Event-ID` 헤더에 마지막 event id 를 설정
//                    .id(String.valueOf(id))
//                    //event data payload (data: SSE connected)
//                    .data("SSE connected")
//                    //SSE 연결이 끊어진 경우 재접속 하기까지 대기 시간 (retry: <RECONNECTION_TIMEOUT>)
//                    .reconnectTime(RECONNECTION_TIMEOUT);
//            emitter.send(event);
//        } catch (IOException e) {
//            log.error("failure send media position data, id={}, {}", id, e.getMessage());
//        }
//        return emitter;
//    }
    public SseEmitter subscribe(String bidId) {
        SseEmitter emitter = createEmitter();
        log.info("Creating new SseEmitter for bidId: {}", bidId);

        emitter.onCompletion(() -> {
            removeEmitter(bidId, emitter);
            log.info("Emitter completed for bidId: {}", bidId);
        });
        emitter.onTimeout(() -> {
            removeEmitter(bidId, emitter);
            log.info("Emitter timeout for bidId: {}", bidId);
        });
        emitter.onError(e -> {
            removeEmitter(bidId, emitter);
            log.error("Error in emitter for bidId: {}, message: {}", bidId, e.getMessage());
        });

        emitterMap.computeIfAbsent(bidId, k -> {
            log.info("Creating new list of SseEmitters for bidId: {}", bidId);
            return new CopyOnWriteArrayList<>();
        }).add(emitter);

        log.info("Added SseEmitter to list for bidId: {}", bidId);

        try {
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    //event 명 (event: event example)
                    .name("event example")
                    //event id (id: id-1) - 재연결시 클라이언트에서 `Last-Event-ID` 헤더에 마지막 event id 를 설정
                    .id(String.valueOf(bidId))
                    //event data payload (data: SSE connected)
                    .data("SSE connected")
                    //SSE 연결이 끊어진 경우 재접속 하기까지 대기 시간 (retry: <RECONNECTION_TIMEOUT>)
                    .reconnectTime(RECONNECTION_TIMEOUT);
            emitter.send(event);
        } catch (IOException e) {
            log.error("failure send media position data, id={}, {}", bidId, e.getMessage());
        }
        return emitter;
    }

    public void broadcast(EventPayload eventPayload) {
        List<SseEmitter> emitters = emitterMap.getOrDefault(eventPayload.bidId(), Collections.emptyList());
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("broadcast event")
                        .id("broadcast event for " + eventPayload.bidId())
                        .reconnectTime(RECONNECTION_TIMEOUT)
                        .data(eventPayload, MediaType.APPLICATION_JSON));
                log.info("Sent notification, bidId={}, payload={}", eventPayload.bidId(), eventPayload);
            } catch (IOException e) {
                log.error("Failed to send emitter for bidId={}, {}", eventPayload.bidId(), e.getMessage());
            }
        }
    }


//    public void broadcast(EventPayload eventPayload) {
//        emitterMap.forEach((id, emitter) -> {
//            try {
//                emitter.send(SseEmitter.event()
//                        .name("broadcast event")
//                        .id("broadcast event 1")
//                        .reconnectTime(RECONNECTION_TIMEOUT)
//                        .data(eventPayload, MediaType.APPLICATION_JSON));
//                log.info("sended notification, id={}, payload={}", id, eventPayload);
//            } catch (IOException e) {
//                //SSE 세션이 이미 해제된 경우
//                log.error("fail to send emitter id={}, {}", id, e.getMessage());
//            }
//        });
//    }

    private void removeEmitter(String bidId, SseEmitter emitter) {
        List<SseEmitter> emitters = emitterMap.get(bidId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                emitterMap.remove(bidId);
                log.info("Removed last emitter, deleting list for bidId: {}", bidId);
            }
            log.info("Removed emitter for bidId={}", bidId);
        }
    }


    private SseEmitter createEmitter() {
        return new SseEmitter(TIMEOUT);
    }

    public void disconnect(DisconnectDTO disconnectDTO) {

    }
}
