package com.vamos.characterlit.chat.dto;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chatrooms")
public class ChatRoomDTO { // FK 수정중입니다.
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long chatroomId;
    Long bidId;
    Long buyerId;
    Long sellerId;
}

