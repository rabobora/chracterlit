package com.vamos.characterlit.chat.service;

import com.vamos.characterlit.chat.dto.ChatRoomDTO;
import com.vamos.characterlit.chat.dto.mongoDBChatRoomDTO;
import com.vamos.characterlit.chat.repository.mongoDBChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class mongoDBChatRoomService {
    private final mongoDBChatRoomRepository mongoDBchatRoomRepository;

    public void createMongoDBChatRoom(Long chatroomId){
        mongoDBChatRoomDTO mongoDBchatRoomDTO=
                mongoDBChatRoomDTO.builder()
                        .chatroomId(chatroomId)
                        .build();

        mongoDBchatRoomRepository.save(mongoDBchatRoomDTO);
    }

}
