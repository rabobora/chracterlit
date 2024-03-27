package com.vamos.characterlit.items.request;

import com.vamos.characterlit.items.domain.Items;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemCreateDto {
    private String title;
    private String content;
    private List<String> image;
    private String thumbnail;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer startBid;
    private Integer category;

    public static Items toEntity(ItemCreateDto itemcreate) {
        return Items.builder()
                .title(itemcreate.getTitle())
                .content(itemcreate.getContent())
                .image(itemcreate.getImage())
                .thumbnail(itemcreate.getThumbnail())
                .startDate(itemcreate.getStartDate())
                .endDate(itemcreate.getEndDate())
                .startBid(itemcreate.getStartBid())
                .category(itemcreate.getCategory())
                .bidStatus(0)
                .viewCount(0)
                .isPaid(false)
                .winnerNumber(null)
                .build();
    }

}

