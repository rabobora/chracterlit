package com.vamos.characterlit.bidtest.domain;

import org.springframework.context.ApplicationEvent;

//이벤트 발생을 나타내는 객체입니다.
public class BidEvent extends ApplicationEvent {

    private final String bidId;
    private final int presentBid;

    // 'source'는 이 이벤트를 발생시킨 객체를 참조합니다.
    // 이는 Spring의 이벤트 시스템에서 요구하는 구조입니다.
    public BidEvent(Object source, String bidItemId, int presentBid) {
        super(source);
        this.bidId = bidItemId;
        this.presentBid = presentBid;
    }

    // Getters
    public String getBidItemId() {
        return bidId;
    }

    public int getPresentBid() {
        return presentBid;
    }
}
