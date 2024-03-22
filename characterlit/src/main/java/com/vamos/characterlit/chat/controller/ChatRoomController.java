package com.vamos.characterlit.chat.controller;

import java.net.URI;

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
//    private final ChatRoomService chatRoomService;
//
//    // 유저가 보유한 채팅방 목록 반환
//    @GetMapping("/chatroomlist/{senderId}")
//    public ResponseEntity chatRoomList(@PathVariable("senderId") Long senderId) {
//        return ResponseEntity.ok(chatRoomService.chatRoomList(senderId));
//    }
//
//    // 채팅방 생성 후 생성된 채팅방 링크 반환
//    @PostMapping("/chatcreate")
//    public ResponseEntity chatRoomCreate(@RequestBody ChatRoomDTO chatRoomDTO) {
//        Long roomId = chatRoomService.createChatRoom(chatRoomDTO);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(roomId)
//                .toUri();
//
//        // header 내 로케이션에 uri 반환
//        return ResponseEntity.created(location).build();
//    }
}
