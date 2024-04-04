package com.vamos.characterlit.bid.repository;

import com.vamos.characterlit.bid.domain.Nowbid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NowbidRepository extends JpaRepository<Nowbid, Long> {

    @Query("SELECT n.presentBid FROM Nowbid n WHERE n.bidId = :bidId")
    Integer findPresentBidByBidId(Long bidId);
}
