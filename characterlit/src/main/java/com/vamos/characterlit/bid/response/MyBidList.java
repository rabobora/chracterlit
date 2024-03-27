package com.vamos.characterlit.bid.response;

import com.vamos.characterlit.bid.domain.Bidlogs;
import com.vamos.characterlit.bid.domain.Nowbid;
import com.vamos.characterlit.items.domain.Items;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyBidList {
    private List<Bidlogs> logList;
    private List<Items> itemsList;
    private List<Nowbid> nowbidList;

    public MyBidList(List<Bidlogs> logList, List<Items> itemsList, List<Nowbid> nowbidList) {
        this.logList = logList;
        this.itemsList = itemsList;
        this.nowbidList = nowbidList;
    }
}
