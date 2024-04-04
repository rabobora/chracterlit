package com.vamos.characterlit.items.service;

import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.request.ItemCreateDto;
import com.vamos.characterlit.items.request.ItemUpdateDto;
import com.vamos.characterlit.items.response.ItemDetailDto;
import com.vamos.characterlit.items.response.ItemListDto;
import com.vamos.characterlit.items.repository.ItemRepository;
import com.vamos.characterlit.users.domain.Users;
import com.vamos.characterlit.users.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final UsersRepository usersRepository;

    // 상품 게시글 생성 -> 로그인 후 로직
    public Items createItem(ItemCreateDto itemCreateDto, Long usernumber) {
        Users users = usersRepository.findByUserNumber(usernumber);

        Items items = ItemCreateDto.toEntity(itemCreateDto, users);
        items.setNickname(users.getNickname());

        return itemRepository.save(items);
    }


    // 상품 게시글 수정
    @Transactional
    public Items updateItem(Long bidId, ItemUpdateDto itemUpdateDto) {
        Items item = itemRepository.findById(bidId)
                .orElseThrow(() -> new EntityNotFoundException("상품 정보를 찾을 수 없습니다: " + bidId));

        // 경매 시작 시간 전에만 수정 가능
        if (item.getStartDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("경매 시작 시간 이후에는 상품 정보를 수정할 수 없습니다.");
        }
        ItemUpdateDto.updateEntity(item, itemUpdateDto);
        return itemRepository.save(item);
    }

    // 전체 상품글 조회
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

    // 특정 상품글 조회
    public ItemDetailDto findItemById(Long bidId) {
        Items items = itemRepository.findById(bidId)
                .orElseThrow(() -> new EntityNotFoundException("상품 정보롤 찾을 수 없습니다: " + bidId));

        // 게시글 조회시 조회수 증가 로직 (item domain 에서 구현된 메소드 사용)
        items.increaseViewCount();
        itemRepository.save(items);

        return ItemDetailDto.fromEntity(items);
    }

    // 상품 게시글 삭제
    public void delete(Long bidId) {
        Items item = itemRepository.findById(bidId)
                .orElseThrow(() -> new EntityNotFoundException("삭제하려는 상품 정보를 찾을 수 없습니다: " + bidId));

        // 경매 시작 시간 전에만 삭제 가능
        if (item.getStartDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("경매 시작 시간 이후에는 상품 정보를 삭제할 수 없습니다.");
        }

        itemRepository.delete(item);
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

    // 카테고리 필터링
    @Transactional(readOnly = true)
    public List<ItemListDto> searchCategories(Integer category) {
        List<Items> categoryItems = itemRepository.findByCategory(category);
        List<ItemListDto> itemDtoList = new ArrayList<>();
        for (Items item : categoryItems) {
            itemDtoList.add(ItemListDto.fromEntity(item));
        }
        return itemDtoList;
    }


    @Transactional(readOnly = true)
    public List<ItemListDto> Top3ItemsByViewCount() {
        List<Items> items = itemRepository.findAll(Sort.by(Sort.Direction.DESC, "viewCount"));
        List<ItemListDto> itemDtoList = new ArrayList<>();

        // 최대 3개의 아이템만 반환
        int count = 0;
        for (Items item : items) {
            if (count >= 3) break;
            ItemListDto itemListDto = ItemListDto.fromEntity(item);
            itemDtoList.add(itemListDto);
            count++;
        }

        return itemDtoList;
    }

}