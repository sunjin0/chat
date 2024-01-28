package com.example.chatuser.controller;

import com.example.chatuser.service.impl.UploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class upLoadController {
    @Autowired
    private UploadServiceImpl uploadService;

    @PostMapping("/post/upload")
    public String upload(MultipartFile file) {
        return uploadService.upload(file);
    }

    @PostMapping("/register/upload")
    public String registerUpload(MultipartFile file) {
        return uploadService.upload(file);
    }
}
