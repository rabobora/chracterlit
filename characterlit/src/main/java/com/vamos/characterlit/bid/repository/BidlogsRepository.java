package com.vamos.characterlit.bid.repository;

import com.vamos.characterlit.bid.domain.Bidlogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidlogsRepository extends JpaRepository<Bidlogs, Long> {
}
