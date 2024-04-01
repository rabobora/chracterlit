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
    private final Map<String, Map<Long, SseEmitter>> emitterMap = new ConcurrentHashMap<>();
    private static final long TIMEOUT = 600 * 1000;
    private static final long RECONNECTION_TIMEOUT = 1000L;
    private final NowbidRepository nowbidRepository;

    public SseEmitterService(NowbidRepository nowbidRepository) {
        this.nowbidRepository = nowbidRepository;
    }

    public SseEmitter subscribe(String bidId, Long sessionId) {
        SseEmitter emitter = createEmitter();
        log.info("Creating new SseEmitter for bidId: {}", bidId);

        emitter.onCompletion(() -> {
            removeEmitter(bidId, sessionId);
            log.info("Emitter completed for bidId: {}, nickname: {}", bidId, sessionId);
        });
        emitter.onTimeout(() -> {
            log.info("Emitter timeout for bidId: {}, nickname: {}", bidId, sessionId);
        });
        emitter.onError(e -> {
            log.error("Error in emitter for bidId: {}, message: {}", bidId, e.getMessage());
        });
        emitterMap.computeIfAbsent(bidId, k -> new ConcurrentHashMap<>()).put(sessionId, emitter);

        log.info("Added SseEmitter to list for bidId: {}, sessionId: {}", bidId, sessionId);

        try {
            Nowbid nowbid = nowbidRepository.findById(Long.parseLong(bidId.substring(5))).orElseThrow(() -> new RuntimeException("Wrong Accepted"));
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .name("first connect")
                    //event id (id: id-1) - 재연결시 클라이언트에서 `Last-Event-ID` 헤더에 마지막 event id 를 설정
                    .id(String.valueOf(bidId))
                    .data("SSE connected as: " + sessionId + " present bid" + nowbid.getPresentBid())
                    .reconnectTime(RECONNECTION_TIMEOUT);
            emitter.send(event);
        } catch (IOException e) {
            log.error("Failure sending initial data for bidId: {}, sessionId: {}, message: {}", bidId, sessionId, e.getMessage());
            emitter.complete();
        }
        return emitter;
    }

    public void broadcast(EventPayload eventPayload) {
        Map<Long, SseEmitter> userEmitters = emitterMap.get("bidId" + eventPayload.bidId());
        log.info("Client" + userEmitters);
        if (userEmitters != null) {
            userEmitters.forEach((sessionId, emitter) -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("bid price update")
                            .id("broadcast event for " + eventPayload.bidId())
                            .reconnectTime(RECONNECTION_TIMEOUT)
                            .data(eventPayload, MediaType.APPLICATION_JSON));
                    log.info("Sent notification, bidId={}, sessionId={}, payload={}", eventPayload.bidId(), sessionId, eventPayload);
                } catch (IOException e) {
                    log.error("Failed to send emitter for bidId={}, sessionId={}, {}", eventPayload.bidId(), sessionId, e.getMessage());
                }
            });
        }
    }

    public void bidSuccessAlarm(EventPayload eventPayload) {
        SseEmitter bidderEmitter = emitterMap.get("bidId" + eventPayload.bidId()).get(eventPayload.sessionId());
        try {
            bidderEmitter.send(SseEmitter.event()
                    .name("bidding success")
                    .id("send event for " + eventPayload.bidId())
                    .reconnectTime(RECONNECTION_TIMEOUT)
                    .data(eventPayload, MediaType.APPLICATION_JSON));
            log.info("Sent BID SUCCESS notification, bidId={}, sessionId={}, userNumber={}", eventPayload.bidId(), eventPayload.sessionId(), eventPayload.userNumber());

        } catch (Exception e) {
            log.error("Failed to send BID SUCCESS emitter for bidId={}, sessionId={}, userNumber={}, {}", eventPayload.bidId(), eventPayload.sessionId(), eventPayload.userNumber(), e.getMessage());
        }
    }
    private void removeEmitter(String bidId, Long sessionId) {
        Map<Long, SseEmitter> userEmitters = emitterMap.get(bidId);
        if (userEmitters != null) {
            userEmitters.remove(sessionId);
            if (userEmitters.isEmpty()) {
                emitterMap.remove(bidId);
                log.info("Removed last emitter, deleting map for bidId: {}", bidId);
            }
            log.info("Removed emitter for bidId={}, sessionId={}", bidId, sessionId);
        }
    }

    private SseEmitter createEmitter() {
        return new SseEmitter(TIMEOUT);
    }

    public void disconnect(DisconnectDTO disconnectDTO) {
        log.info("SSE disconnect request Activated bidId={}, sessionId={}", disconnectDTO.getBidId(), disconnectDTO.getSessionId());
        logEmitterMapSize();
        removeEmitter("bidId" + disconnectDTO.getBidId(), disconnectDTO.getSessionId());
        logEmitterMapSize();
    }

    public void logEmitterMapSize() {
        int totalSize = emitterMap.values().stream().mapToInt(Map::size).sum();
        log.info("Total number of SseEmitters in emitterMap: {}", totalSize);
    }
}
