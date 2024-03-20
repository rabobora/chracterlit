package com.vamos.characterlit.items.request;

import com.vamos.characterlit.items.domain.Items;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemUpdateDto {
    private String title;
    private String content;
    private List<String> image;
    private String thumbnail;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer startBid;
    private Integer category;

    public static void updateEntity(Items item, ItemUpdateDto itemUpdate) {
        if (itemUpdate.getTitle() != null) item.setTitle(itemUpdate.getTitle());
        if (itemUpdate.getContent() != null) item.setContent(itemUpdate.getContent());
        if (itemUpdate.getImage() != null) item.setImage(itemUpdate.getImage());
        if (itemUpdate.getThumbnail() != null) item.setThumbnail(itemUpdate.getThumbnail());
        if (itemUpdate.getStartDate() != null) item.setStartDate(itemUpdate.getStartDate());
        if (itemUpdate.getEndDate() != null) item.setEndDate(itemUpdate.getEndDate());
        if (itemUpdate.getStartBid() != null) item.setStartBid(itemUpdate.getStartBid());
        if (itemUpdate.getCategory() != null) item.setCategory(itemUpdate.getCategory());
    }
}
