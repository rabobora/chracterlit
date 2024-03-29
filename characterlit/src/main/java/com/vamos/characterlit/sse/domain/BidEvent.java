package com.vamos.characterlit.sse.domain;

import org.springframework.context.ApplicationEvent;

//이벤트 발생을 나타내는 객체입니다.
public class BidEvent extends ApplicationEvent {

    private final Long bidId;
    private final Long sessionId;
    private final Long userNumber;
    private final int presentBid;
    private String broadcastType;


    // 'source'는 이 이벤트를 발생시킨 객체를 참조합니다.
    // 이는 Spring의 이벤트 시스템에서 요구하는 구조입니다.
    public BidEvent(Object source, Long bidId, Long sessionId, Long userNumber, int presentBid, String broadcastType) {
        super(source);
        this.bidId = bidId;
        this.sessionId = sessionId;
        this.userNumber = userNumber;
        this.presentBid = presentBid;
        this.broadcastType = broadcastType;
    }

    // Getters
    public Long getBidId() {
        return bidId;
    }
    public Long getSessionId(){
        return sessionId;
    }

    public Long getUserNumber() {
        return userNumber;
    }

    public int getPresentBid() {
        return presentBid;
    }

    public String getBroadcastType(){return broadcastType;}
}
