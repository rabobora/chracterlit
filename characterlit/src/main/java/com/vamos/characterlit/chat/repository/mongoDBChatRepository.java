package com.vamos.characterlit.chat.repository;

import com.vamos.characterlit.chat.dto.mongoDBChatDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface mongoDBChatRepository extends MongoRepository<mongoDBChatDTO, Long> {
    List<mongoDBChatDTO> findByChatroomId(Long chatroomId);
}
