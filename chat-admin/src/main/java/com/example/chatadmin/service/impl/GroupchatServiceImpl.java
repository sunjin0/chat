package com.example.chatadmin.service.impl;

import com.example.chatadmin.service.IGroupchatService;
import com.example.chatadmin.entity.Groupchat;
import com.example.chatadmin.mapper.GroupchatMapper;
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
public class GroupchatServiceImpl extends ServiceImpl<GroupchatMapper, Groupchat> implements IGroupchatService {

}
