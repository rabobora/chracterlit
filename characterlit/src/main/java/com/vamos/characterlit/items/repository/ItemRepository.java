package com.vamos.characterlit.items.repository;

import com.vamos.characterlit.items.domain.Items;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {
    // 키워드 검색
    List<Items> findByTitleContaining(String keyword);

    // 카테고리 필터링
    List<Items> findByCategory(Integer category);

    Items findByBidId(Long bidId);

//    @Transactional
//    @Modifying
//    @Query("UPDATE Items i SET i.bidStatus = 1 WHERE i.startDate <= :now AND i.bidStatus = 0")
//    int updateBidStatusForOpenBids(LocalDateTime now);

    @Query("SELECT i FROM Items i WHERE i.startDate <= :now AND i.bidStatus = 0")
    List<Items> findItemsToOpen(LocalDateTime now);



    @Query("SELECT i FROM Items i WHERE i.endDate <= :now AND i.bidStatus = 1")
    List<Items> findItemsToClose(LocalDateTime now);

    List<Items> findAllByUsersUserNumber(Long userNumber);
}
