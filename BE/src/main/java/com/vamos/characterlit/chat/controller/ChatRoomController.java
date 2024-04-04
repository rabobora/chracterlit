package com.vamos.characterlit.chat.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.vamos.characterlit.auth2.annotation.ExtractPayload;
import com.vamos.characterlit.chat.dto.ChatRoomRequestDTO;
//import com.vamos.characterlit.chat.service.ParticipantsService;
import com.vamos.characterlit.chat.service.mongoDBChatRoomService;
import com.vamos.characterlit.items.domain.Items;
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
        System.out.println("채팅방 생성 요청이 있습니다.");
        if(chatRoomRequestDTO.getBuyerId()==null){
            System.out.println("userNumber가 넘어오지 않았음. 생성하지 않음");
            return ResponseEntity.badRequest().body("not enough userNumber value.");
        }
        ChatRoomDTO chatRoomDTO=ChatRoomDTO.builder()
                .item(itemRepository.findByBidId(chatRoomRequestDTO.getBidId()))
                .user(usersRepository.findByUserNumber(chatRoomRequestDTO.getBuyerId()))
                .build();

        System.out.println(chatRoomRequestDTO);
        System.out.println(chatRoomDTO);

        // 이미 존재하는 채팅방인지 확인
        if(chatRoomService.checkDuplicateChatRooms(chatRoomRequestDTO.getBuyerId(), chatRoomRequestDTO.getBidId())!=null){
            System.out.println("이미 존재하는 채팅방입니다. 생성하지 않습니다.");
            return ResponseEntity.badRequest().body("already exist chat room.");
        }

        // insert into chatroom table
        ChatRoomDTO createdChatRoom= chatRoomService.createChatRoom(chatRoomDTO);

        // mongoDB에도 넣어주기
        mongoDBchatRoomService.createMongoDBChatRoom(createdChatRoom.getChatroomId());

        // http://j10b105.p.ssafy.io:8080/api/chatroom/create/생성된채팅방id
        URI location = ServletUriComponentsBuilder.fromCurrentRequest() // 사용자 요청 uri
                .path("/{id}")
                .buildAndExpand(createdChatRoom.getChatroomId()) // 새로 설정한 id 값. 위 path에 지정됨
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // 사용자 채팅방 목록 조회
    @GetMapping("/api/chatroomlist")
    public ResponseEntity chatRoomList(@ExtractPayload Long buyerId) {
        System.out.println("채팅방을 조회하고자 하는 user:"+buyerId);

        List<ChatRoomDTO> roomAll=new ArrayList<>(); // 구매,판매방 전체

        // 내가 구매자일 경우
        Users user=usersRepository.findByUserNumber(buyerId);

        List<ChatRoomDTO> buyRooms=chatRoomService.selectChatRooms(user);
        for(ChatRoomDTO buy:buyRooms){
            roomAll.add(buy); // 내가 구매하려는 물품의 채팅방
        }

        // 내가 판매자일 경우(해당 bid id의 판매자가 나일 경우)

        // bid_id.user_number(판매자id)가 나인 거 불러오기 -> 내가 파는 items
        List<Items> items=itemRepository.findAllByUsersUserNumber(buyerId);

        if(items.isEmpty()){ // 판매하는 아이템이 없을 경우 리턴
            return ResponseEntity.ok(roomAll);
        }
        // items로 select 하기
        for(Items item:items){
            System.out.println("당신이 판매하고 있는 물품은 아마 "+item.getBidId());

            List<ChatRoomDTO> sellRooms=chatRoomService.selectSellChatRooms(item);
            for(ChatRoomDTO sell:sellRooms){
                System.out.println("해당 물품과 관련 있는 채팅방 정보:"+sell);
                roomAll.add(sell); // 내가 판매하려는 물품의 채팅방
            }
        }
        
        return ResponseEntity.ok(roomAll);
    }
}
