package com.example.chatuser.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.Groupchat;
import com.example.chatuser.entity.Groupchatmember;
import com.example.chatuser.entity.Information;
import com.example.chatuser.mapper.GroupchatmemberMapper;
import com.example.chatuser.service.IGroupchatmemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 群聊成员服务
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @时间 2023/10/26
 * @since 2023-10-05
 */
@Service
public class GroupchatmemberServiceImpl extends ServiceImpl<GroupchatmemberMapper, Groupchatmember> implements IGroupchatmemberService {

    @Autowired
    private GroupchatServiceImpl groupchatService;

    /**
     * 获取群聊列表
     *
     * @param userId 用户标识
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public Object getGroupChatList(Integer userId) {
        ArrayList<Groupchat> list = new ArrayList<>();
        //创建的群聊
        QueryWrapper<Groupchat> wrapper = new QueryWrapper<>();
        wrapper.eq("creatorId", userId);
        List<Groupchat> selfList = groupchatService.list(wrapper);
        if (selfList != null) {
            list.addAll(selfList);
        }
        //加入的群聊
        QueryWrapper<Groupchatmember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Groupchatmember> groupchatmemberList = list(queryWrapper);
        List<Integer> collect = groupchatmemberList.stream().map(Groupchatmember::getGroupChatId).collect(Collectors.toList());
        if (collect.size() != 0) {
            List<Groupchat> groupchats = groupchatService.listByIds(collect);
            if (groupchats != null) {
                list.addAll(groupchats);
            }
        }

        return JSON.toJSONString(list);
    }

    /**
     * 添加群聊成员
     *
     * @param groupchatmember 群聊成员
     * @return {@link Object}
     */
    public Object addGroupChatMember(Groupchatmember groupchatmember) {
        QueryWrapper<Groupchatmember> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
                        .eq("groupChatId", groupchatmember.getGroupChatId()))
                .eq("userId", groupchatmember.getUserId());
        Groupchatmember one = getOne(queryWrapper);
        if (one != null) {
            return Information.toString("info", "已经加入了");
        }
        return save(groupchatmember) ? Information.toString("info", "加入成功") : Information.toString("error", "失败了");
    }

    /**
     * 退出群聊
     *
     * @param chatMember 聊天成员
     * @return {@link String}
     */
    public String exitGroupChat(Groupchatmember chatMember) {
        boolean remove = remove(new QueryWrapper<Groupchatmember>()
                .and(wrapper -> wrapper
                        .eq("groupChatId", chatMember.getGroupChatId())
                        .eq("userId", chatMember.getUserId())));
        return JSON.toJSONString(remove);
    }
}
