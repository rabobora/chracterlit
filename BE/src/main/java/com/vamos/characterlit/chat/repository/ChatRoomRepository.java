package com.vamos.characterlit.chat.repository;

import com.vamos.characterlit.chat.dto.ChatRoomDTO;
import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomDTO, Long> {
//    @Query("SELECT p.userNumber FROM Participants p WHERE p.chatroomId IN " +
//            "(SELECT c.chatroomId FROM Chatrooms c WHERE c.bidId = :bidId) " +
//            "AND p.userId = :userNumber")
//    List<Integer> findUserNumberByBidIdAndUserId(@Param("bidId") Long bidId, @Param("userNumber") Long userId);
    List<ChatRoomDTO> findByUser(Users user);
    List<ChatRoomDTO> findByItem(Items item);
    ChatRoomDTO findByUser_UserNumberAndItem_BidId(Long userNumber, Long bidId);
}
