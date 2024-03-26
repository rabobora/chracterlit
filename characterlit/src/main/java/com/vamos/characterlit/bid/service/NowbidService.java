package com.vamos.characterlit.bid.service;

import com.vamos.characterlit.bid.domain.Bidlogs;
import com.vamos.characterlit.bid.request.BidMessageDTO;
import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.repository.ItemRepository;
import com.vamos.characterlit.sse.domain.BidEvent;
import com.vamos.characterlit.bid.domain.Nowbid;
import com.vamos.characterlit.bid.repository.BidlogsRepository;
import com.vamos.characterlit.bid.repository.NowbidRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NowbidService {
    private final NowbidRepository nowbidRepository;
    private final BidlogsRepository bidlogsRepository;
    private final ItemRepository itemRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void update(BidMessageDTO messageDTO) {
        log.info("Bidding process Activated : bidId={}, requestbid={}",
                messageDTO.getBidId(), messageDTO.getRequestBid());

        Items target = itemRepository.findByBidId(messageDTO.getBidId())
                .orElseThrow(() -> new RuntimeException("Item not found for bidId: " + messageDTO.getBidId()));
        log.info("Bidding Time : bidTime={}", messageDTO.getBidTime());
        log.info("Start Time : bidTime={}", target.getStartDate());
        log.info("End Time : bidTime={}", target.getEndDate());
        if (target.getStartDate().isAfter(messageDTO.getBidTime())
                || target.getEndDate().isBefore(messageDTO.getBidTime())) {
            throw new RuntimeException("Bid time is not within the auction period.");
        }
        if (target.getStartBid() > messageDTO.getRequestBid()) {
            throw new RuntimeException("The bid is lower than the start bid.");
        }
        // 본인이 최상위 입찰자인가 확인 로직
        Optional<Bidlogs> highestBidlog = bidlogsRepository.findTopByBidIdOrderByRequestBidDesc(messageDTO.getBidId());
        if(highestBidlog.isPresent()){
            Bidlogs targetLog = highestBidlog.get();
            if(targetLog.getUserId().equals(messageDTO.getUserId())){
                //예외저치가 아닌 메시지 처리해야하나 임의 작성
                throw new RuntimeException("you are the highest bidder.");
            }
        }

        Optional<Nowbid> nowbidOptional = nowbidRepository.findById(messageDTO.getBidId());
        if (nowbidOptional.isPresent()) {
            Nowbid bidTarget = nowbidOptional.get();
            if (bidTarget.getPresentBid() >= messageDTO.getRequestBid()) {
                throw new RuntimeException("The bid is lower than the current bid.");
            }
            bidTarget.setPresentBid(messageDTO.getRequestBid());
            Bidlogs bidLog = Bidlogs.builder()
                    .bidId(messageDTO.getBidId())
                    .userId(messageDTO.getUserId())
                    .requestBid(messageDTO.getRequestBid())
                    .bidTime(messageDTO.getBidTime())
                    .build();
            nowbidRepository.save(bidTarget);
            bidlogsRepository.save(bidLog);
        } else {
            log.info("First Bidding at bidId={}", messageDTO.getBidId());
            Nowbid newBid = new Nowbid();
            newBid.setBidId(messageDTO.getBidId());
            newBid.setPresentBid(messageDTO.getRequestBid());
            Bidlogs bidLog = Bidlogs.builder()
                    .bidId(messageDTO.getBidId())
                    .userId(messageDTO.getUserId())
                    .requestBid(messageDTO.getRequestBid())
                    .bidTime(messageDTO.getBidTime())
                    .build();
            nowbidRepository.save(newBid);
            bidlogsRepository.save(bidLog);
        }
        log.info("Bidding event process started for bidId={}", messageDTO.getBidId());
        BidEvent event = new BidEvent(this, messageDTO.getBidId(), messageDTO.getUserId(), messageDTO.getRequestBid(), "BROADCAST");
        BidEvent biddingEvent = new BidEvent(this, messageDTO.getBidId(), messageDTO.getUserId(), messageDTO.getRequestBid(), "SINGLE");
        log.info(String.valueOf(event));
        log.info(String.valueOf(biddingEvent));
        eventPublisher.publishEvent(event);
//        eventPublisher.publishEvent(biddingEvent);
    }

    public int readPrice(Long bidId) {
        Optional<Nowbid> nowbidTargetOptional = nowbidRepository.findById(bidId);
        if (nowbidTargetOptional.isEmpty()) {
            throw new RuntimeException("nowbid not exist");
        }
        int presentPrice = nowbidTargetOptional.get().getPresentBid();

        return presentPrice;
    }
}
