package com.vamos.characterlit.bid.service;

import com.vamos.characterlit.bid.repository.BidlogsRepository;
import com.vamos.characterlit.bid.repository.NowbidRepository;
import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledService {

    private final ItemRepository itemRepository;
    private final NowbidRepository nowbidRepository;
    @Transactional
    public void bidClose() {
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        log.info("Closing bids at: {}", now);
        List<Items> itemsToClose = itemRepository.findItemsToClose(now);
        log.info("target count: {}", itemsToClose.size());
        for (Items item : itemsToClose) {
            Integer presentBid = nowbidRepository.findPresentBidByBidId(item.getBidId());
            item.setBidStatus(2);
            item.setFinalBid(presentBid);
            itemRepository.save(item);
        }
    }

    public void bidOpen() {
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        log.info("Opening bids at: {}", now);
        int count = itemRepository.updateBidStatusForOpenBids(now);
        log.info("target count: {}", count);

    }
}
