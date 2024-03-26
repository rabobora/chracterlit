package com.vamos.characterlit.items.response;

import com.vamos.characterlit.items.domain.Items;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ItemDetailDto {
    private Long bidId;
    private String nickname;
    private Long userName;
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
    private Integer category;
    private Integer viewCount;

    @Builder
    public ItemDetailDto(Long bidId, String nickname, Long userName, String title, String content, List<String> image, String thumbnail,
                         LocalDateTime regDate, LocalDateTime startDate, LocalDateTime endDate, Integer startBid, Integer finalBid,
                         Integer bidStatus, Integer category, Integer viewCount) {
        this.bidId = bidId;
        this.nickname = nickname;
        this.userName = userName;
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
        this.category = category;
        this.viewCount = viewCount;
    }

    public static ItemDetailDto fromEntity(Items items) {
        return ItemDetailDto.builder()
                .bidId(items.getBidId())
                .nickname(items.getNickname())
                .userName(items.getUserId())
                .title(items.getTitle())
                .content(items.getContent())
                .image(items.getImage())
                .thumbnail(items.getThumbnail())
                .regDate(items.getRegDate())
                .startDate(items.getStartDate())
                .endDate(items.getEndDate())
                .startBid(items.getStartBid())
                .finalBid(items.getFinalBid())
                .bidStatus(items.getBidStatus())
                .category(items.getCategory())
                .viewCount(items.getViewCount())
                .build();
    }
}
