package com.vamos.characterlit.chat.dto;

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
public class ChatDTO { // mongoDB 적용하면 수정될 예정입니다.
    private Long roomId;
    private Long senderId;
    private String content;
}
