package com.vamos.characterlit.chat.repository;

import com.vamos.characterlit.chat.dto.ChatRoomDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomDTO, Long> {
    List<ChatRoomDTO> findByBuyerIdOrSellerId(Long buyerId, Long sellerId);
}
