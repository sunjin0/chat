package com.example.chatuser.service.impl;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.Browsinghistory;
import com.example.chatuser.entity.Information;
import com.example.chatuser.entity.Post;
import com.example.chatuser.mapper.BrowsinghistoryMapper;
import com.example.chatuser.service.IBrowsinghistoryService;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-10-31
 */
@Service
public class BrowsinghistoryServiceImpl extends ServiceImpl<BrowsinghistoryMapper, Browsinghistory> implements IBrowsinghistoryService {

    @Autowired
    private PostServiceImpl postService;

    /**
     * 按 ID 获取历史记录列表
     *
     * @param userId 用户标识
     * @return {@link String}
     */
    public String getHistoryListById(Integer userId) {
        QueryWrapper<Browsinghistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Browsinghistory> browsingHistoryList = list(queryWrapper);
        //查询帖子内容
        ArrayList<HashMap<String, Object>> maps = new ArrayList<>();
        if (browsingHistoryList.size() != 0) {
            browsingHistoryList.forEach(history -> {
                HashMap<String, Object> hashMap = new HashMap<>();
                QueryWrapper<Post> postQueryWrapper = new QueryWrapper<>();
                postQueryWrapper.select("content").eq("postId", history.getPostId());
                Post one = postService.getOne(postQueryWrapper);
                hashMap.put("history", history);
                hashMap.put("content", one.getContent());
                maps.add(hashMap);
            });
        }
        return maps.size() == 0 ? Information.toString("error", "什么都没有") : JSON.toJSONString(maps);
    }


    /**
     * 添加浏览历史记录
     *
     * @param browsing_history 浏览历史
     * @return {@link String}
     */
    public String addBrowsingHistory(Browsinghistory browsing_history) {
        try {
            Browsinghistory one = getOne(new QueryWrapper<Browsinghistory>().and(wrapper -> wrapper.eq("userId", browsing_history.getUserId()).eq("postId", browsing_history.getPostId())));
            if (one != null) {
                return JSON.toJSONString(false);
            }
        } catch (TooManyResultsException e) {
            return JSON.toJSONString(false);
        }
        browsing_history.setBrowsingTime(LocalDateTime.now());
        return JSON.toJSONString(save(browsing_history));
    }
}
