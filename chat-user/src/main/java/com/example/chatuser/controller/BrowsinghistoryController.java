package com.example.chatuser.controller;


import com.example.chatuser.entity.Browsinghistory;
import com.example.chatuser.service.impl.BrowsinghistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-10-31
 */
@RestController
@RequestMapping("/user/post/history")
public class BrowsinghistoryController {
    @Autowired
    private BrowsinghistoryServiceImpl historyService;

    /**
     * 按 ID 获取历史记录列表
     *
     * @param userId 用户标识
     * @return {@link String}
     */
    @GetMapping("/getHistoryListById/{userId}")
    public String getHistoryListById(@PathVariable Integer userId) {
        return historyService.getHistoryListById(userId);
    }

    @PostMapping("/addBrowsingHistory")
    public String addBrowsingHistory(@RequestBody Browsinghistory browsinghistory) {
        return historyService.addBrowsingHistory(browsinghistory);
    }
}
