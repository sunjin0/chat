package com.example.chatuser.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.CommentReply;
import com.example.chatuser.mapper.CommentReplyMapper;
import com.example.chatuser.service.ICommentReplyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 评论回复表 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-11-03
 */
@Service
public class CommentReplyServiceImpl extends ServiceImpl<CommentReplyMapper, CommentReply> implements ICommentReplyService {

    /**
     * 设置评论回复
     *
     * @param commentReply 评论回复
     * @return {@link String}
     */
    public String setCommentReply(CommentReply commentReply) {
        commentReply.setCreatedTime(LocalDateTime.now());
        return JSON.toJSONString(save(commentReply));
    }

    /**
     * @param commentId 评论 ID
     * @return {@link List}<{@link CommentReply}>
     */
    public List<CommentReply> getCommentReply(Integer commentId) {
        QueryWrapper<CommentReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commentId", commentId);
        return list(queryWrapper);
    }
}
