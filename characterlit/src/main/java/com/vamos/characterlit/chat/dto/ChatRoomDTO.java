package com.vamos.characterlit.chat.dto;

import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.users.domain.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chatrooms")
public class ChatRoomDTO {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long chatroomId;

    @ManyToOne
    @JoinColumn(name="bid_id")
    Items item; // 경매 id

    @ManyToOne
    @JoinColumn(name="user_number")
    Users user; // 구매자 id
}

