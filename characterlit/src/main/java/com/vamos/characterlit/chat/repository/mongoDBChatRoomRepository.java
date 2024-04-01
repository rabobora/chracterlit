package com.vamos.characterlit.chat.repository;

import com.vamos.characterlit.chat.dto.mongoDBChatRoomDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface mongoDBChatRoomRepository extends MongoRepository<mongoDBChatRoomDTO, Long> {
}
