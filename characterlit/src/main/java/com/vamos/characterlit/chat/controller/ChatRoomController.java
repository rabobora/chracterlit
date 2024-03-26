package com.vamos.characterlit.chat.controller;

import java.net.URI;

import com.vamos.characterlit.chat.dto.mongoDBChatRoomDTO;
import com.vamos.characterlit.chat.service.mongoDBChatRoomService;
import org.springframework.http.HttpStatus;
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

        // 이미 생성된 채팅방이 있는지 조회 후 분기
        ChatRoomDTO existChatRoom=chatRoomService.selectChatRoom(
                chatRoomDTO.getBuyerId(),
                chatRoomDTO.getSellerId(),
                chatRoomDTO.getBidId());

        if(existChatRoom!=null){ // 이미 채팅방이 존재
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 채팅방입니다.");
        }

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
