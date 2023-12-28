package com.example.chatadmin.service.impl;

import com.example.chatadmin.service.ITagsService;
import com.example.chatadmin.entity.Tags;
import com.example.chatadmin.mapper.TagsMapper;
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
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {

}
