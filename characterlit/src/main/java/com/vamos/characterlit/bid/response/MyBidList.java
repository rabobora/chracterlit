package com.vamos.characterlit.bid.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyBidList {
    private List<MyLogDTO> logList;
    private List<MyItemDTO> itemsList;
    private List<MyNowbidDTO> nowbidList;

    public MyBidList(List<MyLogDTO> logList, List<MyItemDTO> itemsList, List<MyNowbidDTO> nowbidList) {
        this.logList = logList;
        this.itemsList = itemsList;
        this.nowbidList = nowbidList;
    }
}
