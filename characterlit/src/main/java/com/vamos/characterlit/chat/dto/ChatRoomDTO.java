package com.vamos.characterlit.chat.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name="chatrooms")
public class ChatRoomDTO { // 수정중입니다.
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    Long chatroomId;
    Long sellerId;
    Long buyerId;
}
