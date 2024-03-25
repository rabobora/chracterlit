package com.vamos.characterlit.chat.controller;

import java.net.URI;

import com.vamos.characterlit.chat.dto.mongoDBChatRoomDTO;
import com.vamos.characterlit.chat.service.mongoDBChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vamos.characterlit.chat.dto.ChatRoomDTO;
import com.vamos.characterlit.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final mongoDBChatRoomService mongoDBchatRoomService;

    // 채팅방 생성
    @PostMapping("/api/chatroom/create")
    ResponseEntity createChatRoom(@RequestBody ChatRoomDTO chatRoomDTO){
        System.out.println(chatRoomDTO);
        Long chatroomId = chatRoomService.createChatRoom(chatRoomDTO);

        // mongoDB에도 넣어주기
        mongoDBchatRoomService.createMongoDBChatRoom(chatroomId);

        // http://localhost:8080/api/chatroom/create/생성된채팅방id
        URI location = ServletUriComponentsBuilder.fromCurrentRequest() // 사용자 요청 uri
                .path("/{id}")
                .buildAndExpand(chatroomId) // 새로 설정한 id 값. 위 path에 지정됨
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // 사용자 채팅방 목록 조회
    @GetMapping("/api/chatroomlist/{userId}")
    public ResponseEntity chatRoomList(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(chatRoomService.listChatRoom(userId));
    }
}
