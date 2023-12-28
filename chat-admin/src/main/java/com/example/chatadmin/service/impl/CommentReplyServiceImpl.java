package com.example.chatadmin.service.impl;

import com.example.chatadmin.service.ICommentReplyService;
import com.example.chatadmin.entity.CommentReply;
import com.example.chatadmin.mapper.CommentReplyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论回复表 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-11-09
 */
@Service
public class CommentReplyServiceImpl extends ServiceImpl<CommentReplyMapper, CommentReply> implements ICommentReplyService {

}
