package com.vamos.characterlit.bid.service;

import com.vamos.characterlit.bid.domain.Bidlogs;
import com.vamos.characterlit.bid.domain.Nowbid;
import com.vamos.characterlit.bid.repository.BidlogsRepository;
import com.vamos.characterlit.bid.repository.NowbidRepository;
import com.vamos.characterlit.bid.response.MyBidList;
import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyBidService {

    private final ItemRepository itemsRepository;
    private final BidlogsRepository bidlogsRepository;
    private final NowbidRepository nowbidRepository;

    public MyBidList show(Long userNumber){
        List<Bidlogs> logList = bidlogsRepository.findHighestBidByUser(userNumber);
        List<Long> bidIds = logList.stream().map(Bidlogs::getBidId).collect(Collectors.toList());
        List<Items> itemsList = itemsRepository.findAllById(bidIds);
        List<Nowbid> nowbidList = nowbidRepository.findAllById(bidIds);
        return new MyBidList(logList, itemsList, nowbidList);
    }

    public List<Items> mysell(Long userNumber){
        List<Items> bidSellList = itemsRepository.findAllByUserNumber(userNumber);
        return bidSellList;
    }


}
