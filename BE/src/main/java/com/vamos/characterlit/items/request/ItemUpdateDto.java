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

    // 상품을 수정했을때, 예를 들어 클라이언트가 여러 항목중 카테고리만 수정 요청을 보낸다면
    // 다른 변화 없는 항목들은 그대로 유지하고 카테고리만 수정 가능하게 함
    // 수정할 때는 빌더 패턴을 쓸 필요가 없다해서 여기서는 안 씀
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
