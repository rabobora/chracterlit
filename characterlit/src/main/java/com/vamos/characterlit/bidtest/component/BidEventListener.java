package com.vamos.characterlit.bidtest.component;

import com.vamos.characterlit.bidtest.domain.BidEvent;
import com.vamos.characterlit.bidtest.response.EventPayload;
import com.vamos.characterlit.bidtest.service.SseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;


public class BidEventListener implements ApplicationListener<BidEvent> {
    @Autowired
    private SseEmitterService sseEmitterService;

    @Override
    public void onApplicationEvent(BidEvent event) {
        // SSE를 통해 클라이언트에게 입찰 정보 변경 알림
        EventPayload payload = new EventPayload(
                event.getBidItemId(),
                "exampleMemberName",
                "exampleMemberAge"
        );
        sseEmitterService.broadcast(payload);
    }
}
