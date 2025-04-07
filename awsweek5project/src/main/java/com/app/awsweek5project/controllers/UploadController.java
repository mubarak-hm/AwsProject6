package com.app.awsweek5project.controllers;

import com.app.awsweek5project.AmazonS3.BucketsOperationService;
import com.app.awsweek5project.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadController {

    private final UploadService uploadService;
    private final BucketsOperationService bucketsOperationService;

    public UploadController(UploadService uploadService, BucketsOperationService bucketsOperationService) {
        this.uploadService = uploadService;
        this.bucketsOperationService = bucketsOperationService;
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("imageFile") MultipartFile file,
                              @RequestParam("description") String description,
                              Model model) {
        try {
            uploadService.uploadImage(file, file.getOriginalFilename(), description);
            model.addAttribute("message", "Image uploaded successfully");
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("message", "Image upload failed: " + e.getMessage());
            model.addAttribute("success", false);
        }
        model.addAttribute("images", uploadService.listAll());
        return "image-gallery";
    }

    @GetMapping("/")
    public String displayGallery(Model model) {
        model.addAttribute("images", uploadService.listAll());
        return "image-gallery";
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String key = request.get("key");
            if (key == null || key.isEmpty()) {
                response.put("success", false);
                response.put("message", "Image key is required");
                return ResponseEntity.badRequest().body(response);
            }
            uploadService.deleteImage(key);
            response.put("success", true);
            response.put("message", "Image deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete image: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}