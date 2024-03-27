package com.vamos.characterlit.chat.controller;

import com.vamos.characterlit.chat.service.mongoDBChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vamos.characterlit.chat.dto.mongoDBChatDTO;
import com.vamos.characterlit.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    List<mongoDBChatDTO> testChatStack=new ArrayList<>();

    private final mongoDBChatService mongoDBchatService;;

    // 메시지를 도착지까지 보내는 MessageSendingOperations를 Spring 프레임에 맞춘 것
    private final SimpMessageSendingOperations template;

    // 메시지 불러오기(mongoDB)
    @GetMapping("/api/chat/list/{chatRoomId}")
    public ResponseEntity listMessage(@PathVariable Long chatRoomId){
        return ResponseEntity.ok(mongoDBchatService.listMongoDBChat(chatRoomId));
    }

    // 메시지 전송
    @MessageMapping("/api/chat/{chatRoomId}") // Send Destination Queue
    public void sendMessage(@RequestBody mongoDBChatDTO mongoDBchatDTO) {
        template.convertAndSend("/sub/"+mongoDBchatDTO.getChatroomId(), mongoDBchatDTO);
        testChatStack.add(mongoDBchatDTO);
    }

    // 메시지 저장 테스트
    @GetMapping("/api/chat/save/{chatroomId}")
    public ResponseEntity saveMessage(@PathVariable Long chatroomId){
        mongoDBchatService.saveMongoDBChat(testChatStack);
        testChatStack=new ArrayList<>(); // 초기화

        return ResponseEntity.status(HttpStatus.OK).body("메시지 저장 완료");
    }
    // websocket connection 끊길 시 채팅내역 프론트에서 일괄 저장
}
