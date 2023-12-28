package com.example.chatuser.controller;


import com.example.chatuser.entity.Userrelationship;
import com.example.chatuser.service.impl.UserrelationshipServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-10-14
 */
@RestController
@Slf4j
@RequestMapping("/user/friends")
public class UserrelationshipController {
    @Autowired
    private UserrelationshipServiceImpl relationshipService;

    /**
     * 添加好友
     *
     * @param toke             托克
     * @param userrelationship 用户关系
     * @return {@link Object}
     */
    @PostMapping("/addFriends")
    public Object addFriends(@RequestHeader("Authorization") String toke, @RequestBody Userrelationship userrelationship) {
        log.info(toke);
        return relationshipService.addFriends(userrelationship);
    }

    /**
     * 通过 ID 获取好友状态
     *
     * @param toke 托克
     * @param id   编号
     * @return {@link Object}
     */
    @GetMapping("/getFriendsStateById/{id}")
    public Object getFriendsStateById(@RequestHeader("Authorization") String toke, @PathVariable Integer id) {
        log.info(toke);
        return relationshipService.getFriendsStateById(id);
    }

    /**
     * 通过 ID 设置好友状态
     *
     * @param relationship 关系
     * @return {@link Object}
     */
    @PostMapping("/setFriendsStateById")
    public Object setFriendsStateById(@RequestBody Userrelationship relationship) {
        return relationshipService.setFriendsStateById(relationship);
    }

    /**
     * 结交朋友
     *
     * @param userId 用户标识
     * @return {@link Object}
     */
    @GetMapping("/getFriends/{userId}")
    public Object getFriends(@PathVariable Integer userId) {
        return relationshipService.getFriends(userId);
    }

    /**
     * 通过 ID 获取好友信息
     *
     * @param userrelationship 用户关系
     * @return {@link Object}
     */
    @PostMapping("/getFriendsInfoById")
    public Object getFriendsInfoById(@RequestBody Userrelationship userrelationship) {
        return relationshipService.getFriendsInfoById(userrelationship);
    }

    /**
     * 变更备注
     *
     * @param userrelationship 用户关系
     * @return {@link String}
     */
    @PostMapping("/changeRemarks")
    public String changeRemarks(@RequestBody Userrelationship userrelationship) {
        return relationshipService.changeRemarks(userrelationship);
    }

    @PostMapping("/deleteFriend")
    public String deleteFriend(@RequestBody Userrelationship userrelationship) {
        return relationshipService.deleteFriend(userrelationship);
    }

}
