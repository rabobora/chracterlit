package com.vamos.characterlit.items.repository;

import com.vamos.characterlit.items.domain.ItemDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemDomain, Long> {


}
