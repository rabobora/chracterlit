package com.vamos.characterlit.items.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class StorageService {

    // AWS S3 클라이언트 설정
    private final AmazonS3 amazonS3Client;
    private final String bucketName = "characterlit-bucket";

    public StorageService(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // 파일을 로컬 저장소에 임시 저장
        File fileObj = convertMultiPartToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // S3에 업로드하고 해당 파일 삭제
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();

        // 업로드된 파일의 URL을 반환
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
