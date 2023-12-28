package com.example.chatuser.controller;


import com.example.chatuser.entity.Like;
import com.example.chatuser.service.impl.LikeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 喜欢控制器
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @date 2023/11/01
 * @since 2023-10-05
 */
@RestController
@RequestMapping(value = "/user/like")
public class LikeController {

    @Autowired
    private LikeServiceImpl likeService;

    /**
     * 按 ID 获取点赞列表
     *
     * @param userId 用户标识
     * @return {@link String}
     */
    @GetMapping("/getLikeListById/{userId}")
    public String getLikeListById(@PathVariable Integer userId) {
        return likeService.getLikeListById(userId);
    }

    /**
     * 按帖子ID设置Like
     *
     * @param like 喜欢
     * @return {@link String}
     */
    @PostMapping("/setLikeByPostId")
    public String setLikeByPostId(@RequestBody Like like) {
        return likeService.setLikeByPostId(like);
    }

    @PostMapping("/deleteLikeByUserId")
    public String deleteLikeByUserId(@RequestBody Like like) {
        return likeService.deleteLikeByUserId(like);
    }
}
