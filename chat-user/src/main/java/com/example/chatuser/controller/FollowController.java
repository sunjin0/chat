package com.example.chatuser.controller;


import com.example.chatuser.entity.Follow;
import com.example.chatuser.service.impl.FollowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-11-02
 */
@RestController
@RequestMapping("/user/follow")
public class FollowController {

    @Autowired
    private FollowServiceImpl followService;

    @PostMapping("/setFollowing")
    public String setFollowing(@RequestBody Follow follow) {
        return followService.setFollowing(follow);
    }

    @PostMapping("/cancelFollowing")
    public String cancelFollowing(@RequestBody Follow follow) {
        return followService.cancelFollowing(follow);
    }
}
