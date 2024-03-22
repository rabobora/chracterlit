package com.vamos.characterlit.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vamos.characterlit.chat.dto.ChatRoomDTO;

public interface ChatRoomRepository extends JpaRepository<ChatRoomDTO, Long> {

}
