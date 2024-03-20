package com.vamos.characterlit.items.service;

import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.request.ItemCreateDto;
import com.vamos.characterlit.items.request.ItemUpdateDto;
import com.vamos.characterlit.items.response.ItemDetailDto;
import com.vamos.characterlit.items.response.ItemListDto;
import com.vamos.characterlit.items.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public Items createItem(ItemCreateDto itemCreateDto) {
        Items items = ItemCreateDto.toEntity(itemCreateDto);
        // db에 글 저장
        return itemRepository.save(items);

    }

    public Items updateItem(Long bidId, ItemUpdateDto itemUpdateDto) {
        Items item = itemRepository.findById(bidId)
                .orElseThrow(() -> new EntityNotFoundException("상품 정보를 찾을 수 없습니다: " + bidId));
        ItemUpdateDto.updateEntity(item, itemUpdateDto);
        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<ItemListDto> findAllItems() {
        List<Items> items = itemRepository.findAll();
        List<ItemListDto> itemDtoList = new ArrayList<>();
        for (Items item : items) {
            ItemListDto itemListDto = ItemListDto.fromEntity(item);
            itemDtoList.add(itemListDto);
        }
        return itemDtoList;
    }


    public ItemDetailDto findItemById(Long bidId) {
        Items items = itemRepository.findById(bidId)
                .orElseThrow(() -> new EntityNotFoundException("상품 정보롤 찾을 수 없습니다: " + bidId));

        // 게시글 조회시 조회수 증가
        items.increaseViewCount();
        itemRepository.save(items);

        return ItemDetailDto.fromEntity(items);
    }

    public void delete(Long bidId) {
        try {
            itemRepository.deleteById(bidId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("삭제하려는 상품 정보를 찾을 수 없습니다: " + bidId);
        }
    }

    // 키워드 검색
    @Transactional(readOnly = true)
    public List<ItemListDto> searchItems(String keyword) {
        List<Items> searchResults = itemRepository.findByTitleContaining(keyword);
        List<ItemListDto> itemDtoList = new ArrayList<>();
        for (Items item : searchResults) {
            itemDtoList.add(ItemListDto.fromEntity(item));
        }
        return itemDtoList;
    }

    @Transactional(readOnly = true)
    public List<ItemListDto> searchCategories(Integer category) {
        List<Items> items = itemRepository.findByCategory(category);
        return items.stream()
                .map(ItemListDto::fromEntity)
                .collect(Collectors.toList());
    }
}
