package com.vamos.characterlit.chat.dto;

import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomRequestDTO {
    Long bidId;
    Long buyerId;
}
