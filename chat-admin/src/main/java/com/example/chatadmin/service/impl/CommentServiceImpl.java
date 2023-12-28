package com.example.chatadmin.service.impl;

import com.example.chatadmin.service.ICommentService;
import com.example.chatadmin.entity.Comment;
import com.example.chatadmin.mapper.CommentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-11-09
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
