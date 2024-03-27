package com.vamos.characterlit.chat.dto;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection="chatmessages")
public class mongoDBChatDTO { // mongoDB 적용하면 수정될 예정입니다.
    @Id
    Long chatroomId;
    Long senderId;
    String content;
}
