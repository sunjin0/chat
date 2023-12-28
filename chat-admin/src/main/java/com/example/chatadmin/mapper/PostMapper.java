package com.example.chatadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.chatadmin.entity.Post;
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
public interface PostMapper extends BaseMapper<Post> {

}
