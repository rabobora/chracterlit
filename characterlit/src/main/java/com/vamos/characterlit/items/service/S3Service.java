package com.vamos.characterlit.items.service;

import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.repository.ItemRepository;
import com.vamos.characterlit.items.service.StorageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final ItemRepository itemRepository;
    private final StorageService storageService;

    public S3Service(ItemRepository itemRepository, StorageService storageService) {
        this.itemRepository = itemRepository;
        this.storageService = storageService;
    }

    // s3이미지 업로드
    public List<String> uploadImages(List<MultipartFile> files) throws IOException {
        List<String> uploadedImageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String imageUrl = storageService.uploadFile(file); // StorageService에 정의된 uploadFile을 호출
            uploadedImageUrls.add(imageUrl);
        }
        return uploadedImageUrls;
    }

    //s3 이미지 일괄 수정
    public void updateItemImages(Long itemId, List<MultipartFile> newImageFiles) throws IOException {
        Items item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("해당상품을 찾을 수 없습니다: " + itemId));

        // 기존 이미지들을 S3에서 삭제
        List<String> oldImageUrls = item.getImage();
        for (String oldImageUrl : oldImageUrls) {
            storageService.deleteFile(oldImageUrl);
        }

        // 새 이미지를 S3에 업로드하고 URL 저장
        List<String> newImageUrls = uploadImages(newImageFiles);
        item.setImage(newImageUrls);
        itemRepository.save(item);
    }

    // s3 이미지 일괄 삭제
    public void deleteItemImages(Long itemId) {
        Items item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("해당상품을 찾을 수 없습니다: " + itemId));

        // S3에서 이미지들 삭제
        List<String> imageUrls = item.getImage();
        for (String imageUrl : imageUrls) {
            storageService.deleteFile(imageUrl);
        }

        // 이미지 URL 리스트를 비우고 업데이트
        item.setImage(new ArrayList<>());
        itemRepository.save(item);
    }
}
