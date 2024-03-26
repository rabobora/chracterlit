package com.vamos.characterlit.bid.repository;

import com.vamos.characterlit.bid.domain.Bidlogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidlogsRepository extends JpaRepository<Bidlogs, Long> {
    @Query("SELECT b FROM Bidlogs b WHERE b.bidId = :bidId ORDER BY b.requestBid DESC")
    Optional<Bidlogs> findTopByBidIdOrderByRequestBidDesc(Long bidId);
}
