package com.vamos.characterlit.items.response;

import com.vamos.characterlit.items.domain.Items;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ItemListDto {
    private Long bidId;
    private String nickname;
    private Integer userId;

    private String title;
    private String thumbnail;
    private LocalDateTime regDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer startBid;
    private Integer finalBid;
    private Integer bidStatus;
    private Integer category;

    @Builder
    public ItemListDto(Long bidId, String nickname, Integer userId, String title, String thumbnail,
                       LocalDateTime regDate, LocalDateTime startDate, LocalDateTime endDate, Integer startBid,
                       Integer finalBid, Integer bidStatus, Integer category) {
        this.bidId = bidId;
        this.nickname = nickname;
        this.userId = userId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.regDate = regDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startBid = startBid;
        this.finalBid = finalBid;
        this.bidStatus = bidStatus;
        this.category = category;
    }

    // Entity -> DTO 변환 메소드
    public static ItemListDto fromEntity(Items items) {
        return ItemListDto.builder()
                .bidId(items.getBidId())
                .nickname(items.getNickname())
                .userId(items.getUserId())
                .title(items.getTitle())
                .thumbnail(items.getThumbnail())
                .regDate(items.getRegDate())
                .startDate(items.getStartDate())
                .endDate(items.getEndDate())
                .startBid(items.getStartBid())
                .finalBid(items.getFinalBid())
                .bidStatus(items.getBidStatus())
                .category(items.getCategory())
                .build();
    }
}
