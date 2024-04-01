package com.vamos.characterlit.items.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ThumbnailStorageService {

    private final AmazonS3 amazonS3Client;
    private final String bucketName = "thumbnail-characterlit";

    public ThumbnailStorageService(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String uploadThumbnail(MultipartFile file) throws IOException {
        File fileObj = convertMultiPartToFile(file);
        String fileName = "images/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(bucketName, fileName);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}
