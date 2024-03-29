package com.vamos.characterlit.sse.component;

import com.vamos.characterlit.sse.domain.BidEvent;
import com.vamos.characterlit.sse.response.EventPayload;
import com.vamos.characterlit.sse.service.SseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BidEventListener implements ApplicationListener<BidEvent> {
    @Autowired
    private SseEmitterService sseEmitterService;

    @Override
    public void onApplicationEvent(BidEvent event) {
        log.info("Event Activated");
        // SSE를 통해 클라이언트에게 입찰 정보 변경 알림
        EventPayload payload = new EventPayload(
                event.getBidId(),
                event.getSessionId(),
                event.getUserNumber(),
                event.getPresentBid()
        );
        if(event.getBroadcastType().equals("BROADCAST")){
            sseEmitterService.broadcast(payload);
        } else if (event.getBroadcastType().equals("SINGLE")){
            sseEmitterService.bidSuccessAlarm(payload);
        }
    }
}
