package com.vamos.characterlit.chat.service;

import com.vamos.characterlit.chat.dto.ChatRoomDTO;
import com.vamos.characterlit.chat.repository.ChatRoomRepository;
import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.repository.ItemRepository;
import com.vamos.characterlit.users.domain.Users;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomDTO createChatRoom(ChatRoomDTO chatRoomDTO){
        return chatRoomRepository.save(chatRoomDTO);
    }
    public List<ChatRoomDTO> selectChatRooms(Users user){
        return chatRoomRepository.findByUser(user);
    }

    public List<ChatRoomDTO> selectSellChatRooms(Items item) {
        return chatRoomRepository.findByItem(item);
    }

    public ChatRoomDTO checkDuplicateChatRooms(Long userNumber, Long bidId){
        return chatRoomRepository.findByUser_UserNumberAndItem_BidId(userNumber, bidId);
    }
}
