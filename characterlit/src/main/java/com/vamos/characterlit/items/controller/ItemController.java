package com.vamos.characterlit.items.controller;


import com.vamos.characterlit.auth2.annotation.ExtractPayload;
//import com.vamos.characterlit.auth2.security.jwt.JWTUtil;
import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.request.ItemCreateDto;
import com.vamos.characterlit.items.request.ItemUpdateDto;
import com.vamos.characterlit.items.response.ItemDetailDto;
import com.vamos.characterlit.items.response.ItemListDto;
import com.vamos.characterlit.items.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bid")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    // 상품 글 생성


    @PostMapping("/create")
    public ResponseEntity<?> createItem(@RequestBody ItemCreateDto request, @ExtractPayload Long userNumber ) {
        return ResponseEntity.ok(itemService.createItem(request, userNumber));
    }

    // 전체 상품 조회
    @GetMapping("/search/all")
    public ResponseEntity<List<ItemListDto>> findAllItems() {
        List<ItemListDto> items = itemService.findAllItems();
        return ResponseEntity.ok(items);
    }

    // 특정 상품 상세조회
    @GetMapping("/read/{bidid}")
    public ResponseEntity<ItemDetailDto> findItemById(@PathVariable Long bidid) {
        ItemDetailDto itemDetail = itemService.findItemById(bidid);
        return ResponseEntity.ok(itemDetail);
    }

    // 상품 글 수정
    @PutMapping("/modify/{bidid}")
    public ResponseEntity<?> updateItem(@PathVariable Long bidid, @RequestBody ItemUpdateDto itemUpdateDto) {
        Items updatedItem = itemService.updateItem(bidid, itemUpdateDto);
        return ResponseEntity.ok(updatedItem);
    }

    //상품 글 삭제
    @DeleteMapping("/delete/{bidid}")
    public ResponseEntity<?> deleteItem(@PathVariable Long bidid) {
        try {
            itemService.delete(bidid);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 상품 제목 기반 키워드 검색
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ItemListDto>> searchItem(@PathVariable String keyword) {
        List<ItemListDto> searchResults = itemService.searchItems(keyword);
        return ResponseEntity.ok(searchResults);
    }

    // 상품 카테고리 필터링
    @GetMapping("/search/category/{categoryid}")
    public ResponseEntity<List<ItemListDto>> searchCategory(@PathVariable Integer categoryid) {
        List<ItemListDto> searchCategoryResults = itemService.searchCategories(categoryid);
        return ResponseEntity.ok(searchCategoryResults);
    }

    // 조회수 top3 항목 출력
    @GetMapping("/search/top3")
    public ResponseEntity<List<ItemListDto>> Top3ItemsByViewCount() {
        List<ItemListDto> items = itemService.Top3ItemsByViewCount();
        return ResponseEntity.ok(items);
    }


}