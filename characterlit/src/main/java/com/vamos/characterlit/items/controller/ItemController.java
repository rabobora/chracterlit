package com.vamos.characterlit.items.controller;


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

    @PostMapping("/create") // 상품 등록
    public ResponseEntity<?> createItem(@RequestBody ItemCreateDto request) {
        System.out.println(request.toString());
        return ResponseEntity.ok(itemService.createItem(request));
    }

    @GetMapping("/search/all") // 전체글 조회
    public ResponseEntity<List<ItemListDto>> findAllItems() {
        List<ItemListDto> items = itemService.findAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/read/{bidid}")// 특정 게시글 상세조회
    public ResponseEntity<ItemDetailDto> findItemById(@PathVariable Long bidid) {
        ItemDetailDto itemDetail = itemService.findItemById(bidid);
        return ResponseEntity.ok(itemDetail);
    }

    @PutMapping("/modify/{bidid}")
    public ResponseEntity<?> updateItem(@PathVariable Long bidid, @RequestBody ItemUpdateDto itemUpdateDto) {
        Items updatedItem = itemService.updateItem(bidid, itemUpdateDto);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/delete/{bidid}")
    public ResponseEntity<?> deleteItem(@PathVariable Long bidid) {
        try {
            itemService.delete(bidid);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ItemListDto>> searchItem(@PathVariable String keyword) {
        List<ItemListDto> searchResults = itemService.searchItems(keyword);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/search/category/{categoryid}")
    public ResponseEntity<List<ItemListDto>> searchCategory(@PathVariable Integer categoryid) {
        List<ItemListDto> searchCategoryResults = itemService.searchCategories(categoryid);
        return ResponseEntity.ok(searchCategoryResults);
    }


}
