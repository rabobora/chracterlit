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
    private Long userNumber;
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
    public ItemListDto(Long bidId, String nickname, Long userNumber, String title, String thumbnail,
                       LocalDateTime regDate, LocalDateTime startDate, LocalDateTime endDate, Integer startBid,
                       Integer finalBid, Integer bidStatus, Integer category) {
        this.bidId = bidId;
        this.nickname = nickname;
        this.userNumber = userNumber;
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

    // Entity를 DTO로 변환하는 메소드
    // 메소드명은 통상적으로 사용되는 것으로 명명함
    public static ItemListDto fromEntity(Items items) {
        // Users 엔티티를 미리 초기화하거나 필요한 정보를 조회하는 로직이 필요합니다.
        Long userNumber = (items.getUsers() != null) ? items.getUsers().getUserNumber() : null;
        return ItemListDto.builder()
                .bidId(items.getBidId())
                .nickname(items.getNickname())
                .userNumber(userNumber) // 수정된 부분
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
