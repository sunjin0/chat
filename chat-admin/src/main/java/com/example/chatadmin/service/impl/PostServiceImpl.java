package com.example.chatadmin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatadmin.entity.Browsinghistory;
import com.example.chatadmin.entity.Post;
import com.example.chatadmin.entity.Tags;
import com.example.chatadmin.mapper.PostMapper;
import com.example.chatadmin.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-11-09
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {

    @Autowired
    private TagsServiceImpl tagsService;

    @Autowired
    private BrowsinghistoryServiceImpl historyService;

    /**
     * 获取帖子列表
     *
     * @param current 当前
     * @return {@link String}
     */
    public String obtainPostList(Integer current) {
        Page<Post> page = page(new Page<>(current, 8));
        postAdd(page);
        return JSON.toJSONString(page);
    }

    private void postAdd(Page<Post> page) {
        List<Tags> tags = tagsService.list();
        ArrayList<Post> posts = new ArrayList<>();
        page.getRecords().forEach(post -> {
            tags.forEach(tag -> {
                if (post.getTagId().equals(tag.getTagId())) {
                    post.setTag(tag.getName());
                    posts.add(post);
                }
            });
        });
        page.setRecords(posts);
    }

    /**
     * 更改帖子信息
     *
     * @param post 发布
     * @return {@link String}
     */
    public String changePostInformation(Post post) {
        boolean update = update(post, new UpdateWrapper<Post>().eq("postId", post.getPostId()));
        return JSON.toJSONString(update);
    }


    /**
     * 删除帖子
     *
     * @param postId 帖子 ID
     * @return {@link String}
     */
    public String deletePost(Integer postId) {
        return JSON.toJSONString(removeById(postId));
    }


    /**
     * 搜索帖子
     *
     * @param postId  帖子 ID
     * @param userId  用户 ID
     * @param current 当前
     * @return {@link String}
     */
    public String searchPost(String postId, String userId, Integer current) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        if (!postId.equals("null")) queryWrapper.eq("postId", postId);
        if (!userId.equals("null")) queryWrapper.eq("userId", userId);
        Page<Post> page = page(new Page<>(current, 8), queryWrapper);
        postAdd(page);
        return JSON.toJSONString(page);
    }

    public String visualizingData() {
        // 创建QueryWrapper对象
        QueryWrapper<Browsinghistory> queryWrapper = new QueryWrapper<>();

        // 设置查询条件：时间范围为一个月内
        queryWrapper.ge("browsingTime", LocalDate.now().minusMonths(1));

        // 分组统计帖子的浏览次数，并按浏览次数降序排列
        queryWrapper.select("postId, COUNT(*) as view_count")
                .groupBy("postId")
                .orderByDesc("view_count")
                .last("LIMIT 5");

        // 执行查询
        List<Map<String, Object>> maps = historyService.getBaseMapper().selectMaps(queryWrapper);
        maps.forEach(history -> history.put("history", historyList((Integer) history.get("postId"))));
        return JSON.toJSONString(maps);
    }

    public Map<String, Long> historyList(Integer postId) {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        QueryWrapper<Browsinghistory> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("postId", postId)
                .ge("browsingTime", startDate)
                .lt("browsingTime", endDate.plusDays(1)); // 使用 lt 方法确保小于结束日期的第二天

        List<Browsinghistory> browsingHistories = historyService.getBaseMapper().selectList(queryWrapper2);

        Map<LocalDate, Long> dailyViewCountMap = browsingHistories.stream()
                .collect(Collectors.groupingBy(
                        history -> history.getBrowsingTime().toLocalDate(), // 将日期时间转换为日期
                        Collectors.counting()
                ));

        // 补齐一个月内没有浏览记录的日期，将其对应的浏览次数设置为 0
        Map<String, Long> result = new LinkedHashMap<>();
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            result.put(date.toString().substring(5), dailyViewCountMap.getOrDefault(date, 0L));
            date = date.plusDays(1);
        }
        return result;
    }

}
