package com.example.chatuser;

import com.example.chatuser.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatUserApplicationTests {
    @Autowired
    private PostServiceImpl postService;

    @Test
    void contextLoads() {


    }
}
