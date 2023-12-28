package com.example.chatadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.chatadmin.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 孙进
 * @since 2023-11-09
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
