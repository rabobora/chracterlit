package com.vamos.characterlit.chat.controller;

import java.net.URI;

import com.vamos.characterlit.auth2.annotation.ExtractPayload;
import com.vamos.characterlit.chat.dto.ChatRoomRequestDTO;
//import com.vamos.characterlit.chat.service.ParticipantsService;
import com.vamos.characterlit.chat.service.mongoDBChatRoomService;
import com.vamos.characterlit.items.repository.ItemRepository;
import com.vamos.characterlit.users.domain.Users;
import com.vamos.characterlit.users.repository.UsersRepository;
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
    private final ItemRepository itemRepository;
    private final UsersRepository usersRepository;

    // 채팅방 생성
    @PostMapping("/api/chatroom/create")
    ResponseEntity createChatRoom(@RequestBody ChatRoomRequestDTO chatRoomRequestDTO){
        ChatRoomDTO chatRoomDTO=ChatRoomDTO.builder()
                .item(itemRepository.findByBidId(chatRoomRequestDTO.getBidId()))
                .user(usersRepository.findByUserNumber(chatRoomRequestDTO.getBuyerId()))
                .build();

        System.out.println(chatRoomRequestDTO);
        System.out.println(chatRoomDTO);

        // insert into chatroom table
        ChatRoomDTO createdChatRoom= chatRoomService.createChatRoom(chatRoomDTO);

        // mongoDB에도 넣어주기
        mongoDBchatRoomService.createMongoDBChatRoom(createdChatRoom.getChatroomId());

        // http://localhost:8080/api/chatroom/create/생성된채팅방id
        URI location = ServletUriComponentsBuilder.fromCurrentRequest() // 사용자 요청 uri
                .path("/{id}")
                .buildAndExpand(createdChatRoom.getChatroomId()) // 새로 설정한 id 값. 위 path에 지정됨
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // 사용자 채팅방 목록 조회
    @GetMapping("/api/chatroomlist")
    public ResponseEntity chatRoomList(@ExtractPayload Long buyerId) {
        System.out.println(buyerId);

        Users user=usersRepository.findByUserNumber(buyerId);

        return ResponseEntity.ok(chatRoomService.selectChatRooms(user));
    }
}
