package com.example.chatuser.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.Groupchat;
import com.example.chatuser.entity.Information;
import com.example.chatuser.mapper.GroupchatMapper;
import com.example.chatuser.service.IGroupchatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 群聊服务
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-10-05
 */
@Service
public class GroupchatServiceImpl extends ServiceImpl<GroupchatMapper, Groupchat> implements IGroupchatService {
    /**
     * 创建群聊
     *
     * @param groupchat 群聊
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public Object createGroupChat(Groupchat groupchat) {
        groupchat.setCreateTime(LocalDateTime.now());
        boolean save = save(groupchat);

        return save ? Information.toString("info", "创建成功") : Information.toString("error", "创建失败");

    }

    /**
     * 按 ID 获取群聊
     *
     * @param groupId 组标识
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public Object getGroupChatById(Integer groupId) {
        Groupchat groupchat = getById(groupId);
        if (groupchat != null) {
            return groupchat;
        }
        return Information.toString("error", "没有该群...");
    }

    /**
     * 删除群聊
     *
     * @param groupchat 群聊
     * @return {@link String}
     */
    public String deleteGroupChat(Groupchat groupchat) {
        boolean remove = remove(new QueryWrapper<Groupchat>()
                .and(wrapper -> wrapper
                        .eq("creatorId", groupchat.getCreatorId())
                        .eq("groupChatId", groupchat.getGroupChatId())));
        return JSON.toJSONString(remove);
    }

}
