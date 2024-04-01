package com.vamos.characterlit.bid.service;

import com.vamos.characterlit.bid.domain.Bidlogs;
import com.vamos.characterlit.bid.domain.Nowbid;
import com.vamos.characterlit.bid.repository.BidlogsRepository;
import com.vamos.characterlit.bid.repository.NowbidRepository;
import com.vamos.characterlit.bid.response.*;
import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.repository.ItemRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public MyBidList show(Long userNumber){
        List<Bidlogs> logList = bidlogsRepository.findHighestBidByUser(userNumber);
        List<Long> bidIds = logList.stream().map(Bidlogs::getBidId).collect(Collectors.toList());
        List<Items> itemsList = itemsRepository.findAllById(bidIds);
        List<Nowbid> nowbidList = nowbidRepository.findAllById(bidIds);

        List<MyLogDTO> logListDTO = convertToMyLogDTO(logList);
        List<MyItemDTO> itemsListDTO = convertToMyItemDTO(itemsList);
        List<MyNowbidDTO> nowbidListDTO = convertToMyNowbidDTO(nowbidList);


        return new MyBidList(logListDTO, itemsListDTO, nowbidListDTO);
    }



    @Transactional
    public List<MySellListDTO> mysell(Long userNumber){
        List<Items> bidSellList = itemsRepository.findAllByUsersUserNumber(userNumber);
        return convertToDtoList(bidSellList);
    }

    public List<MySellListDTO> convertToDtoList(List<Items> itemsList) {
        return itemsList.stream()
                .map(item -> new MySellListDTO(item.getBidId(),
                        item.getUsers() != null ? item.getUsers().getUserNumber() : null,
                        item.getNickname(), item.getCategory(), item.getTitle(),
                        item.getContent(), item.getImage(), item.getThumbnail(),
                        item.getRegDate(), item.getStartDate(), item.getEndDate(),
                        item.getStartBid(), item.getFinalBid(), item.getBidStatus(),
                        item.getViewCount(), item.getIsPaid(), item.getWinnerNumber()))
                .collect(Collectors.toList());
    }
    private List<MyNowbidDTO> convertToMyNowbidDTO(List<Nowbid> nowbidList) {
        return nowbidList.stream()
                .map(nowbid -> new MyNowbidDTO(
                        nowbid.getBidId(),
                        nowbid.getPresentBid()))
                .collect(Collectors.toList());
    }

    private List<MyItemDTO> convertToMyItemDTO(List<Items> itemsList) {
        return itemsList.stream().map(item -> new MyItemDTO(
                item.getBidId(),
                item.getUsers() != null ? item.getUsers().getUserNumber() : null,
                item.getNickname(),
                item.getCategory(),
                item.getTitle(),
                item.getContent(),
                item.getThumbnail(),
                item.getRegDate(),
                item.getStartDate(),
                item.getEndDate(),
                item.getStartBid(),
                item.getFinalBid(),
                item.getBidStatus(),
                item.getViewCount(),
                item.getIsPaid(),
                item.getWinnerNumber()
        )).collect(Collectors.toList());
    }

    private List<MyLogDTO> convertToMyLogDTO(List<Bidlogs> logList) {
        return logList.stream()
                .map(bidLog -> new MyLogDTO(
                        bidLog.getBidId(),
                        bidLog.getUserNumber(),
                        bidLog.getRequestBid()))
                .collect(Collectors.toList());
    }

}
