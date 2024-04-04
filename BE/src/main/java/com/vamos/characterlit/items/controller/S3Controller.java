package com.vamos.characterlit.items.controller;

import com.vamos.characterlit.items.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {
    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    // 이미지 업로드후 URL 목록을 list<string> 형태로 반환
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("image") List<MultipartFile> image) throws IOException {
        List<String> imageUrls = s3Service.uploadImages(image);
        return ResponseEntity.ok(imageUrls);
    }

    // 특정 게시글의 이미지 수정
    @PutMapping("/update/{bidid}")
    public ResponseEntity<?> updateItemImages(@PathVariable Long bidid, @RequestParam("image") List<MultipartFile> image) throws IOException {
        s3Service.updateItemImages(bidid, image);
        return ResponseEntity.ok().build();
    }

    // 특정 게시글의 모든 이미지를 삭제.
    @DeleteMapping("/delete/{bidid}")
    public ResponseEntity<?> deleteItemImages(@PathVariable Long bidid) {
        s3Service.deleteItemImages(bidid);
        return ResponseEntity.ok().build();
    }

}
