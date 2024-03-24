package com.vamos.characterlit.items.repository;

import com.vamos.characterlit.items.domain.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {
    // 키워드 검색
    List<Items> findByTitleContaining(String keyword);

    // 카테고리 필터링
    List<Items> findByCategory(Integer category);

    Optional<Items> findByBidId(Long bidId);
}
