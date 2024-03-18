package com.vamos.characterlit.bidtest.service;

import com.vamos.characterlit.bidtest.domain.BidEvent;
import com.vamos.characterlit.bidtest.domain.Nowbid;
import com.vamos.characterlit.bidtest.repository.BidlogsRepository;
import com.vamos.characterlit.bidtest.repository.NowbidRepository;
import com.vamos.characterlit.bidtest.request.BidRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
    public void update(BidRequestDTO bidRequestDTO) {
        Optional<Nowbid> nowbidOptional = nowbidRepository.findById(bidRequestDTO.getBidId());
        if(nowbidOptional.isPresent()){
            Nowbid topbid = nowbidOptional.get();
            if(topbid.getPresentBid() < bidRequestDTO.getRequestBid()) {
                topbid.setPresentBid(bidRequestDTO.getRequestBid());
            } else {
                throw new RuntimeException("The bid is lower than the current top bid.");
            }
        } else {
            Nowbid newBid = new Nowbid();
            newBid.setBidId(bidRequestDTO.getBidId());
            newBid.setPresentBid(bidRequestDTO.getRequestBid());
            nowbidRepository.save(newBid);
        }
        BidEvent event = new BidEvent(this, bidRequestDTO.getBidId(), bidRequestDTO.getRequestBid());
        eventPublisher.publishEvent(event);
    }
}
