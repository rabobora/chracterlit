package com.vamos.characterlit.items.controller;

import com.vamos.characterlit.items.service.LambdaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/lambda")
public class LambdaController {

    private final LambdaService lambdaService;

    public LambdaController(LambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadThumbnail(@RequestParam("thumbnail") MultipartFile thumbnail) throws Exception {
        String thumbnailUrl = lambdaService.uploadThumbnail(thumbnail);
        return ResponseEntity.ok(thumbnailUrl);
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<String> updateThumbnail(@PathVariable Long itemId, @RequestParam("thumbnail") MultipartFile thumbnail) throws Exception {
        String thumbnailUrl = lambdaService.updateThumbnail(itemId, thumbnail);
        return ResponseEntity.ok(thumbnailUrl);
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteThumbnail(@PathVariable Long itemId) {
        lambdaService.deleteThumbnail(itemId);
        return ResponseEntity.ok().build();
    }
}
