package com.example.chatuser.controller;


import com.example.chatuser.annotation.Sanction;
import com.example.chatuser.entity.Post;
import com.example.chatuser.service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * 发布控制器
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @date 2023/10/27
 * @since 2023-10-05
 */
@RestController
@RequestMapping(value = "/user/post")
public class PostController {

    @Autowired
    private PostServiceImpl postService;


    /**
     * 添加帖子
     *
     * @param post 发布
     * @return {@link String}
     */
    @Sanction
    @PostMapping("/addOrUpdatePost")
    public String addPost(@RequestBody Post post) {
        post.setPublishTime(LocalDateTime.now());
        return (String) postService.addPost(post);
    }

    /**
     * 按 ID 获取帖子列表
     *
     * @param postId 帖子 ID
     * @return {@link String}
     */
    @GetMapping("/getPostListById/{postId}")
    public String getPostListById(@PathVariable Integer postId) {
        return postService.getPostListById(postId);
    }

    /**
     * 根据帖子 ID 删除帖子
     *
     * @param postId 帖子 ID
     * @return {@link String}
     */
    @GetMapping("/deletePostByPostId/{postId}")
    public String deletePostByPostId(@PathVariable Integer postId) {
        return postService.deletePostByPostId(postId);
    }

    /**
     * 新用户与否
     *
     * @param userId 用户标识
     * @return boolean
     */
    @GetMapping("/newUserOrNot/{userId}")
    public boolean newUserOrNot(@PathVariable Integer userId) {
        return postService.newUserOrNot(userId);
    }

    /**
     * 根据标签推荐
     *
     * @param map 标签ID数组，分页页码
     * @return {@link String}
     */
    @PostMapping("/recommendedBasedOnLabels")
    public String recommendedBasedOnLabels(@RequestBody HashMap<String, Object> map) {
        return postService.recommendedBasedOnLabels((List<Integer>) map.get("array"), (Integer) map.get("current"), (Integer) map.get("userId"));
    }

    /**
     * 按 ID 获取帖子
     *
     * @param postId 帖子 ID
     * @return {@link String}
     */
    @GetMapping("/getPostById/{postId}")
    public String getPostById(@PathVariable Integer postId) {
        return postService.getPostById(postId);
    }

    /**
     * 推荐帖子
     *
     * @param userId 用户 ID
     * @return {@link String}
     */
    @GetMapping("/recommendPost/{userId}")
    public String recommendPost(@PathVariable Long userId) {
        return postService.recommendPost(userId);
    }

    /**
     * 模糊查询帖子
     *
     * @param code 法典
     * @return {@link String}
     */
    @GetMapping("/fuzzyQueryPost/{code}")
    public String fuzzyQueryPost(@PathVariable String code) {
        return postService.fuzzyQueryPost(code);
    }

    @GetMapping("/followRecommendations/{userId}")
    public String followRecommendations(@PathVariable Integer userId) {
        return postService.followRecommendations(userId);
    }


}
