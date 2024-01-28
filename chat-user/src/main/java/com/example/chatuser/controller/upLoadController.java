package com.example.chatuser.controller;

import com.example.chatuser.service.impl.UploadServiceImpl;
import com.example.chatuser.service.util.VideoFormatStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Slf4j
public class upLoadController {
    @Autowired
    private UploadServiceImpl uploadService;

    @PostMapping("/post/upload")
    public String upload(MultipartFile file) {
        String type = file.getOriginalFilename();
        log.error(type);
        assert type != null;
        VideoFormatStrategy.VideoFormat videoFormat = VideoFormatStrategy.getVideoFormat(type);
        if (videoFormat == VideoFormatStrategy.VideoFormat.UNKNOWN) {
            return uploadService.upload(file, "image");
        }
        return uploadService.upload(file, "video");
    }

    @PostMapping("/register/upload")
    public String registerUpload(MultipartFile file) {

        return uploadService.upload(file, "image");
    }

    @PostMapping("/post_medium")
    public String upLoadVideo(MultipartFile file) {
        return uploadService.upload(file, "video");

    }

    @PostMapping("/chat_voice")
    public String upLoadVoice(MultipartFile file) {
        return uploadService.upload(file, "voice");
    }
}
