package com.vamos.characterlit.items.service;

import com.vamos.characterlit.items.domain.Items;
import com.vamos.characterlit.items.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LambdaService {
    private final ItemRepository itemRepository;
    private final ThumbnailStorageService thumbnailStorageService;

    public LambdaService(ItemRepository itemRepository, ThumbnailStorageService thumbnailStorageService) {
        this.itemRepository = itemRepository;
        this.thumbnailStorageService = thumbnailStorageService;
    }

    public String uploadThumbnail(MultipartFile thumbnail) throws Exception {
        return thumbnailStorageService.uploadThumbnail(thumbnail);
    }

    public String updateThumbnail(Long itemId, MultipartFile newThumbnail) throws Exception {
        Items item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("해당상품을 찾을 수 없습니다: " + itemId));

        if(item.getThumbnail() != null && !item.getThumbnail().isEmpty()) {
            String oldFileName = extractFileName(item.getThumbnail());
            thumbnailStorageService.deleteFile(oldFileName);
        }

        String newThumbnailUrl = thumbnailStorageService.uploadThumbnail(newThumbnail);
        item.setThumbnail(newThumbnailUrl);
        itemRepository.save(item);

        return newThumbnailUrl;
    }


    public void deleteThumbnail(Long itemId) {
        Items item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("해당상품을 찾을 수 없습니다: " + itemId));

        if(item.getThumbnail() != null && !item.getThumbnail().isEmpty()) {
            String fileName = extractFileName(item.getThumbnail());
            thumbnailStorageService.deleteFile(fileName);
            item.setThumbnail(null);
            itemRepository.save(item);
        }
    }

    private String extractFileName(String fileUrl) {
        if (fileUrl != null && !fileUrl.isEmpty()) {
            int indexOfSlash = fileUrl.lastIndexOf("/");
            if (indexOfSlash >= 0) {
                return fileUrl.substring(indexOfSlash + 1);
            }
        }
        return fileUrl;
    }
}
