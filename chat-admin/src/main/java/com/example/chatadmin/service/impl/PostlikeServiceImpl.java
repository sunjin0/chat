package com.example.chatadmin.service.impl;

import com.example.chatadmin.service.IPostlikeService;
import com.example.chatadmin.entity.Postlike;
import com.example.chatadmin.mapper.PostlikeMapper;
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
public class PostlikeServiceImpl extends ServiceImpl<PostlikeMapper, Postlike> implements IPostlikeService {

}
