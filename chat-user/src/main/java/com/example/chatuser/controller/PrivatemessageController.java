package com.example.chatuser.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.chatuser.entity.Privatemessage;
import com.example.chatuser.service.impl.PrivatemessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 私人消息控制器
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-10-05
 */
@Slf4j
@RestController
@RequestMapping(value = "/user/privateMessage")
public class PrivatemessageController {
    @Autowired
    private PrivatemessageServiceImpl privatemessageService;

    /**
     * 添加私信
     *
     * @param privatemessage 私信
     * @return {@link Object}
     */
    @PostMapping("/addPrivateMessage")
    public Object addPrivateMessage(@RequestBody Privatemessage privatemessage) {
        return privatemessageService.addPrivateMessage(privatemessage);
    }

    /**
     * 通过 ID 获取私人消息
     *
     * @param privatemessage 私信
     * @return {@link Object}
     */
    @PostMapping("/getPrivateMessageById")
    public Object getPrivateMessageById(@RequestBody Privatemessage privatemessage) {
        log.info(String.valueOf(privatemessage));
        Page<Privatemessage> page = new Page<>(Integer.parseInt(privatemessage.getContent()), 10);
        return privatemessageService.getPrivateMessageById(page, privatemessage);
    }
}
