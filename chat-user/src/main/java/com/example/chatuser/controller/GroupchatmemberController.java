package com.example.chatuser.controller;


import com.example.chatuser.entity.Groupchatmember;
import com.example.chatuser.service.impl.GroupchatmemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 群聊成员控制器
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-10-05
 */
@Slf4j
@RestController
@RequestMapping(value = "/user/groupChatMember")
public class GroupchatmemberController {
    @Autowired
    private GroupchatmemberServiceImpl groupchatmemberService;

    /**
     * 获取群聊列表
     *
     * @param userId 用户标识
     * @return {@link Object}
     */
    @GetMapping("/getGroupChatList/{userId}")
    public Object getGroupChatList(@PathVariable Integer userId) {

        return groupchatmemberService.getGroupChatList(userId);
    }

    /**
     * 添加群聊成员
     *
     * @param groupchatmember 群聊成员
     * @return {@link Object}
     */
    @PostMapping("/addGroupChatMember")
    public Object addGroupChatMember(@RequestBody Groupchatmember groupchatmember) {

        return groupchatmemberService.addGroupChatMember(groupchatmember);
    }

    /**
     * 退出群聊
     *
     * @param chatMember 聊天成员
     * @return {@link String}
     */
    @PostMapping("/exitGroupChat")
    public String exitGroupChat(@RequestBody Groupchatmember chatMember) {
        return groupchatmemberService.exitGroupChat(chatMember);
    }

}
