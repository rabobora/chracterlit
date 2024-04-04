package com.vamos.characterlit.chat.service;

import com.vamos.characterlit.chat.dto.mongoDBChatDTO;
import com.vamos.characterlit.chat.repository.mongoDBChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class mongoDBChatService {
    private final mongoDBChatRepository mongoDBchatRepository;

    public List<mongoDBChatDTO> listMongoDBChat(Long chatroomId){
        return mongoDBchatRepository.findByChatroomId(chatroomId);
    }
    public void saveMongoDBChatList(List<mongoDBChatDTO> mongoDBchatDTOList){
        mongoDBchatRepository.saveAll(mongoDBchatDTOList);
    }

    public void saveMongoDBChat(mongoDBChatDTO mongoDBchatDTO){
        mongoDBchatRepository.save(mongoDBchatDTO);
    }
}
