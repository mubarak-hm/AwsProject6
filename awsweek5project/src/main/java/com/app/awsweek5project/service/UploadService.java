package com.app.awsweek5project.service;

import com.app.awsweek5project.AmazonS3.BucketsOperationService;
import com.app.awsweek5project.DTO.ImageDTO;
import com.app.awsweek5project.Entity.Image;
import com.app.awsweek5project.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UploadService {

    @Value("${aws.s3-bucket}")
    private String bucketName;
    private final BucketsOperationService bucketsOperationService;
    private final ImageRepository imageRepository;

    public UploadService(BucketsOperationService bucketsOperationService, ImageRepository imageRepository) {
        this.bucketsOperationService = bucketsOperationService;
        this.imageRepository = imageRepository;
    }

    public void uploadImage(MultipartFile file, String key, String description) throws IOException {
        bucketsOperationService.addObject(file, key);
        String url = "https://" +bucketName+ ".s3.eu-central-1.amazonaws.com/"+key;
        Image image = new Image(key, url, description);
        System.out.println(url);
        imageRepository.save(image);

    }

    public List<ImageDTO> listAll() {
        return imageRepository.findAll().stream()
                .map(image -> new ImageDTO(image.getName(), image.getUrl(), image.getDescription()))
                .collect(Collectors.toList());
    }

    public void deleteImage(String key) {
        bucketsOperationService.removeObject(key);
        imageRepository.deleteByName(key);
    }
}