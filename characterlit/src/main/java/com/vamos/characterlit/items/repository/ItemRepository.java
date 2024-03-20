package com.vamos.characterlit.items.repository;

import com.vamos.characterlit.items.domain.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {
    List<Items> findByTitleContaining(String keyword);

    List<Items> findByCategory(Integer category);
}
