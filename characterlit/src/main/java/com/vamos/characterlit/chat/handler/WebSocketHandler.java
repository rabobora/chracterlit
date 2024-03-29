package com.vamos.characterlit.chat.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    //websocket handshake가 완료되어 연결이 수립될 때 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("connection established, session id={}", session.getId());
        sessionMap.putIfAbsent(session.getId(), session);
    }

    //websocket 오류가 발생했을 때 호출
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("session transport exception, session id={}, error={}", session.getId(), exception.getMessage());
        sessionMap.remove(session.getId());
    }

    //websocket 세션 연결이 종료되었을 때 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("connection closed, sesson id={}, close status={}", session.getId(), status);
        sessionMap.remove(session.getId());
    }
}