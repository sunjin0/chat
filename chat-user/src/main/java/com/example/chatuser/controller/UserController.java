package com.example.chatuser.controller;


import com.alibaba.fastjson.JSON;
import com.example.chatuser.entity.User;
import com.example.chatuser.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-10-05
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    /**
     * 注册
     *
     * @param user 用户
     * @return {@link String}
     */
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 获取代码
     *
     * @param user 用户
     * @return {@link String}
     */
    @PostMapping("/code")
    public String getCode(@RequestBody User user) {
        return userService.getCode(user);
    }

    /**
     * 登录
     *
     * @param user 用户
     * @return {@link String}
     */
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return JSON.toJSONString(userService.login(user));
    }

    /**
     * 搜索好友
     *
     * @param idOrEmail 用户ID或者Email
     * @return {@link String}
     * @apiNote 使用toke验证身份后，使用ID或者Email查询新的朋友
     * @since 2023-10-12
     */
    @ApiOperation(value = "", httpMethod = "GET", notes = "使用toke验证身份后，使用ID或者Email查询新的朋友")
    @GetMapping("/searchUser/{idOrEmail}")
    public String searchUser(@PathVariable String idOrEmail) {
        return JSON.toJSONString(userService.searchUser(new User().setEmail(idOrEmail.trim())));
    }

    /**
     * 按 ID 获取用户信息
     *
     * @param userId 用户标识
     * @return {@link String}
     */
    @GetMapping("/getUserInfoById/{userId}")
    public String getUserInfoById(@PathVariable Integer userId) {
        return userService.getUserInfoById(userId);
    }

    /**
     * 按 ID 更新用户信息
     *
     * @param user 用户
     * @return {@link String}
     */
    @PostMapping("/updateUserInfoById")
    public String updateUserInfoById(@RequestBody User user) {
        return (String) userService.updateUserInfoById(user);
    }

    /**
     * 更改密码
     *
     * @param user 用户
     * @return {@link String}
     */
    @PostMapping("/changePassword")
    public String changePassword(@RequestBody User user) {
        return userService.changePassword(user);
    }


}
