package com.vamos.characterlit.bid.repository;

import com.vamos.characterlit.bid.domain.Bidlogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidlogsRepository extends JpaRepository<Bidlogs, Long> {
//    @Query("SELECT b FROM Bidlogs b WHERE b.bidId = :bidId ORDER BY b.requestBid DESC")
    Optional<Bidlogs> findTop1ByBidIdOrderByRequestBidDesc(Long bidId);

    @Query("SELECT b FROM Bidlogs b WHERE b.bidId IN " +
            "(SELECT b2.bidId FROM Bidlogs b2 WHERE b2.userNumber = :userNumber GROUP BY b2.bidId) " +
            "AND b.requestBid IN " +
            "(SELECT MAX(b3.requestBid) FROM Bidlogs b3 WHERE b3.userNumber = :userNumber AND b3.bidId = b.bidId GROUP BY b3.bidId)")
    List<Bidlogs> findHighestBidByUser(@Param("userNumber") Long userNumber);
}
