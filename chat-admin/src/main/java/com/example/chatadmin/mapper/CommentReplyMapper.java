package com.example.chatadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.chatadmin.entity.CommentReply;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 评论回复表 Mapper 接口
 * </p>
 *
 * @author 孙进
 * @since 2023-11-09
 */
@Mapper
public interface CommentReplyMapper extends BaseMapper<CommentReply> {

}
