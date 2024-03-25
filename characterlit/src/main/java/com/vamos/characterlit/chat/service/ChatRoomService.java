package com.vamos.characterlit.chat.service;

import com.vamos.characterlit.chat.dto.ChatRoomDTO;
import com.vamos.characterlit.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public Long createChatRoom(ChatRoomDTO chatRoomDTO){
        return chatRoomRepository.save(chatRoomDTO).getChatroomId();
    }

    public List<ChatRoomDTO> listChatRoom(Long userId){
        return chatRoomRepository.findByBuyerIdOrSellerId(userId, userId);
    }
}
