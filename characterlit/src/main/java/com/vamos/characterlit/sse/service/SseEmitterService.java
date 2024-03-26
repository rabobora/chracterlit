package com.vamos.characterlit.sse.service;

import com.vamos.characterlit.bid.domain.Nowbid;
import com.vamos.characterlit.bid.repository.NowbidRepository;
import com.vamos.characterlit.sse.request.DisconnectDTO;
import com.vamos.characterlit.sse.response.EventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseEmitterService {
    private final Map<String, Map<String, SseEmitter>> emitterMap = new ConcurrentHashMap<>();
    private static final long TIMEOUT = 600 * 1000;
    private static final long RECONNECTION_TIMEOUT = 1000L;
    private final NowbidRepository nowbidRepository;

    public SseEmitterService(NowbidRepository nowbidRepository) {
        this.nowbidRepository = nowbidRepository;
    }

    public SseEmitter subscribe(String bidId, String nickname) {
        SseEmitter emitter = createEmitter();
        log.info("Creating new SseEmitter for bidId: {}", bidId);

        emitter.onCompletion(() -> {
            removeEmitter(bidId, nickname);
            log.info("Emitter completed for bidId: {}, nickname: {}", bidId, nickname);
        });
        emitter.onTimeout(() -> {
//            removeEmitter(bidId, nickname);
            log.info("Emitter timeout for bidId: {}, nickname: {}", bidId, nickname);
        });
        emitter.onError(e -> {
//            removeEmitter(bidId, nickname);
            log.error("Error in emitter for bidId: {}, message: {}", bidId, e.getMessage());
        });
        emitterMap.computeIfAbsent(bidId, k -> new ConcurrentHashMap<>()).put(nickname, emitter);

        log.info("Added SseEmitter to list for bidId: {}, nickname: {}", bidId, nickname);

        try {
            Nowbid nowbid = nowbidRepository.findById(Long.parseLong(bidId.substring(5))).orElseThrow(() -> new RuntimeException("Wrong Accepted"));
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    //event 명 (event: event example)
                    .name("event example")
                    //event id (id: id-1) - 재연결시 클라이언트에서 `Last-Event-ID` 헤더에 마지막 event id 를 설정
                    .id(String.valueOf(bidId))
                    //event data payload (data: SSE connected)
                    .data("SSE connected as: " + nickname + "present bid" + nowbid.getPresentBid())
                    //SSE 연결이 끊어진 경우 재접속 하기까지 대기 시간 (retry: <RECONNECTION_TIMEOUT>)
                    .reconnectTime(RECONNECTION_TIMEOUT);
            emitter.send(event);
        } catch (IOException e) {
            log.error("Failure sending initial data for bidId: {}, nickname: {}, message: {}", bidId, nickname, e.getMessage());
        }
        return emitter;
    }

    public void broadcast(EventPayload eventPayload) {
        Map<String, SseEmitter> userEmitters = emitterMap.get("bidId" + eventPayload.bidId());
        log.info("Client" + userEmitters);
        if (userEmitters != null) {
            userEmitters.forEach((nickname, emitter) -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("bid price update")
                            .id("broadcast event for " + eventPayload.bidId())
                            .reconnectTime(RECONNECTION_TIMEOUT)
                            .data(eventPayload, MediaType.APPLICATION_JSON));
                    log.info("Sent notification, bidId={}, nickname={}, payload={}", eventPayload.bidId(), nickname, eventPayload);
                } catch (IOException e) {
                    log.error("Failed to send emitter for bidId={}, nickname={}, {}", eventPayload.bidId(), nickname, e.getMessage());
                }
            });
        }
    }

//    public void bidSuccessAlarm(EventPayload eventPayload) {
//        SseEmitter bidderEmitter = emitterMap.get("bidId" + eventPayload.bidId()).get(eventPayload.userId());
//        try {
//            bidderEmitter.send(SseEmitter.event()
//                    .name("bidding success")
//                    .id("send event for " + eventPayload.bidId())
//                    .reconnectTime(RECONNECTION_TIMEOUT)
//                    .data(eventPayload, MediaType.APPLICATION_JSON));
//            log.info("Sent BID SUCCESS notification, bidId={}, userId={}", eventPayload.bidId(), eventPayload.userId());
//
//        } catch (Exception e) {
//            log.error("Failed to send BID SUCCESS emitter for bidId={}, userId={}, {}", eventPayload.bidId(), eventPayload.userId(), e.getMessage());
//        }
//    }
    private void removeEmitter(String bidId, String nickname) {
        Map<String, SseEmitter> userEmitters = emitterMap.get(bidId);
        if (userEmitters != null) {
            userEmitters.remove(nickname);
            if (userEmitters.isEmpty()) {
                emitterMap.remove(bidId);
                log.info("Removed last emitter, deleting map for bidId: {}", bidId);
            }
            log.info("Removed emitter for bidId={}, nickname={}", bidId, nickname);
        }
    }


    private SseEmitter createEmitter() {
        return new SseEmitter(TIMEOUT);
    }

    public void disconnect(DisconnectDTO disconnectDTO) {
        log.info("SSE disconnect request Activated bidId={}, nickname={}", disconnectDTO.getBidId(), disconnectDTO.getNickname());
        removeEmitter("bidId" + disconnectDTO.getBidId(), disconnectDTO.getNickname());
    }
}
