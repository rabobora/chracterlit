package com.vamos.characterlit.bid.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MySellListDTO {

    private Long bidId;
    private Long userNumber;
    private String nickname;
    private Integer category;
    private String title;
    private String content;
    private List<String> image;
    private String thumbnail;
    private LocalDateTime regDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer startBid;
    private Integer finalBid;
    private Integer bidStatus;
    private Integer viewCount;
    private Boolean isPaid;
    private Long winnerNumber;


    public MySellListDTO(Long bidId, Long userNumber, String nickname, Integer category, String title,
                         String content, List<String> image, String thumbnail, LocalDateTime regDate,
                         LocalDateTime startDate, LocalDateTime endDate, Integer startBid, Integer finalBid,
                         Integer bidStatus, Integer viewCount, Boolean isPaid, Long winnerNumber) {
        this.bidId = bidId;
        this.userNumber = userNumber;
        this.nickname = nickname;
        this.category = category;
        this.title = title;
        this.content = content;
        this.image = image;
        this.thumbnail = thumbnail;
        this.regDate = regDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startBid = startBid;
        this.finalBid = finalBid;
        this.bidStatus = bidStatus;
        this.viewCount = viewCount;
        this.isPaid = isPaid;
        this.winnerNumber = winnerNumber;
    }


}
