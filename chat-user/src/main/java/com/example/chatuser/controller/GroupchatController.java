package com.example.chatuser.controller;


import com.example.chatuser.entity.Groupchat;
import com.example.chatuser.service.impl.GroupchatServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 群聊控制器
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @date 2023/10/22
 * @since 2023-10-05
 */
@RestController
@Slf4j
@RequestMapping(value = "/user/groupChat")
public class GroupchatController {
    @Autowired
    private GroupchatServiceImpl groupchatService;

    /**
     * 创建群聊
     *
     * @param groupchat 群聊
     * @return {@link com.alibaba.fastjson2.JSON}
     */
    @PostMapping("/createGroupChat")
    public Object createGroupChat(@RequestBody Groupchat groupchat) {
        return groupchatService.createGroupChat(groupchat);
    }

    /**
     * 按 ID 获取群聊
     *
     * @param groupId 组标识
     * @return {@link Object}
     */
    @GetMapping("/getGroupChat/{groupId}")
    public Object getGroupChatById(@PathVariable Integer groupId) {
        return groupchatService.getGroupChatById(groupId);
    }

    /**
     * 删除群聊
     *
     * @param groupchat 群聊
     * @return {@link String}
     */
    @PostMapping("/deleteGroupChat")
    public String deleteGroupChat(@RequestBody Groupchat groupchat) {
        return groupchatService.deleteGroupChat(groupchat);
    }

}
