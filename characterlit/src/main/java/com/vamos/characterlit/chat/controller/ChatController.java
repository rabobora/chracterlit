package com.vamos.characterlit.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vamos.characterlit.chat.dto.ChatDTO;
import com.vamos.characterlit.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    // 메시지를 도착지까지 보내는 MessageSendingOperations를 Spring 프레임에 맞춘 것
    private final SimpMessageSendingOperations template;

    @MessageMapping("/chatroom/{roomId}") // Send Destination Queue
    public void sendMessage(@RequestBody ChatDTO chatDTO) {

    }
}
