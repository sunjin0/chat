package com.example.chatuser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.chatuser.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 孙进
 * @since 2023-10-05
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
