package com.example.chatuser.controller;


import com.example.chatuser.entity.CommentReply;
import com.example.chatuser.service.impl.CommentReplyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 评论回复表 前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-11-03
 */
@RestController
@RequestMapping("/post/comment-reply")
public class CommentReplyController {
    @Autowired
    private CommentReplyServiceImpl commentReplyService;

    @PostMapping("/setCommentReply")
    public String setCommentReply(@RequestBody CommentReply commentReply) {
        return commentReplyService.setCommentReply(commentReply);
    }
}
