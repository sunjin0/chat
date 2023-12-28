package com.example.chatadmin;

import com.example.chatadmin.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatAdminApplicationTests {
    @Autowired
    private PostServiceImpl postService;

    @Test
    void contextLoads() {
        postService.visualizingData();
    }

}
