package com.vamos.characterlit.chat.dto;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection="chatrooms")
public class mongoDBChatRoomDTO { // FK 수정중입니다.
    @Id
    Long chatroomId;
}

