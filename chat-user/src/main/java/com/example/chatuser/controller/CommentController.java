package com.example.chatuser.controller;


import com.example.chatuser.entity.Comment;
import com.example.chatuser.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注释控制器
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @date 2023/10/22
 * @since 2023-10-05
 */
@RestController
@RequestMapping(value = "/post/comment")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    /**
     * 通过帖子 ID 获取评论
     *
     * @param current 当前
     * @param postId  帖子 ID
     * @return {@link String}
     */
    @GetMapping("/getCommentByPostId/{postId}/{current}")
    public String getCommentByPostId(@PathVariable Integer current, @PathVariable Integer postId) {
        return commentService.getCommentByPostId(postId, current);
    }

    /**
     * 按帖子 ID 设置评论
     *
     * @param comment 评论
     * @return {@link String}
     */
    @PostMapping("/setCommentByPostId")
    public String setCommentByPostId(@RequestBody Comment comment) {
        return commentService.setCommentByPostId(comment);
    }
}
