package com.vamos.characterlit.bidtest.repository;

import com.vamos.characterlit.bidtest.domain.Bidlogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidlogsRepository extends JpaRepository<Bidlogs, Long> {
}
