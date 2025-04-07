package com.app.awsweek5project.Request;

import org.springframework.web.multipart.MultipartFile;

public record UploadRequest(MultipartFile file ) {
}
