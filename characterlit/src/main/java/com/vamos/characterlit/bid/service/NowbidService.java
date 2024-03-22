package com.vamos.characterlit.bid.service;

import com.vamos.characterlit.bid.domain.Bidlogs;
import com.vamos.characterlit.sse.domain.BidEvent;
import com.vamos.characterlit.bid.domain.Nowbid;
import com.vamos.characterlit.bid.repository.BidlogsRepository;
import com.vamos.characterlit.bid.repository.NowbidRepository;
import com.vamos.characterlit.bid.request.BidRequestDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class NowbidService {
    private final NowbidRepository nowbidRepository;
    private final BidlogsRepository bidlogsRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public NowbidService(NowbidRepository nowbidRepository, BidlogsRepository bidlogsRepository) {
        this.nowbidRepository = nowbidRepository;
        this.bidlogsRepository = bidlogsRepository;
    }
    @Transactional
    public void update(Long bidId, BidRequestDTO bidRequestDTO) {
        // bidlogs에 대한 검증 처리 없습니다. 추가 필요.
        log.info("Bidding process Activated : bidId={}, requestbid={}",bidId, bidRequestDTO.getRequestBid());
        Optional<Nowbid> nowbidOptional = nowbidRepository.findById(bidId);
        if(nowbidOptional.isPresent()){
            Nowbid topbid = nowbidOptional.get();

            if(topbid.getPresentBid() < bidRequestDTO.getRequestBid()) {
                topbid.setPresentBid(bidRequestDTO.getRequestBid());
                Bidlogs bidLog =Bidlogs.builder()
                        .bidId(bidId)
                        .requestBid(bidRequestDTO.getRequestBid())
                        .bidTime(bidRequestDTO.getBidTime())
                        .build();

                nowbidRepository.save(topbid);
                bidlogsRepository.save(bidLog);
            } else {
                throw new RuntimeException("The bid is lower than the current top bid.");
            }
        } else {
            Nowbid newBid = new Nowbid();
            newBid.setBidId(bidId);
            newBid.setPresentBid(bidRequestDTO.getRequestBid());
            Bidlogs bidLog =Bidlogs.builder()
                    .bidId(bidId)
                    .requestBid(bidRequestDTO.getRequestBid())
                    .bidTime(bidRequestDTO.getBidTime())
                    .build();
            nowbidRepository.save(newBid);
            bidlogsRepository.save(bidLog);
        }
        log.info("Bidding Event Process Start");
        BidEvent event = new BidEvent(this, bidId, bidRequestDTO.getUserId(), bidRequestDTO.getRequestBid());
        log.info(String.valueOf(event));
        eventPublisher.publishEvent(event);
    }
}
