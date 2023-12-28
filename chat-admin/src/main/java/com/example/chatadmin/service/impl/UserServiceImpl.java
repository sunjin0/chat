package com.example.chatadmin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatadmin.entity.Information;
import com.example.chatadmin.entity.Post;
import com.example.chatadmin.entity.Tags;
import com.example.chatadmin.entity.User;
import com.example.chatadmin.mapper.UserMapper;
import com.example.chatadmin.service.IUserService;
import com.example.chatadmin.util.BCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-11-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private BCryptUtil bCryptUtil;

    @Autowired
    private PrivatemessageServiceImpl privatemessageService;

    @Autowired
    private TagsServiceImpl tagsService;

    @Autowired
    private PostServiceImpl postService;

    public String login(User user) {
        User one = getOne(new QueryWrapper<User>().and(wrapper -> wrapper.eq("username", user.getUsername()).eq("temporary", user.getTemporary())));
        if (one == null) {
            return Information.toString("error", "用户不存在！");
        }
        if (!bCryptUtil.matches(user.getPassword(), one.getPassword())) {
            return Information.toString("error", "密码错误！");
        }
        return JSON.toJSONString(one.setPassword(""));
    }


    /**
     * 可视化数据
     *
     * @return {@link String}
     */
    public String visualizingData() {
        HashMap<String, Object> map = new HashMap<>();
        Long userCount = getBaseMapper().selectCount(null);
        map.put("userCount", userCount);
        Long messageCount = privatemessageService.getBaseMapper().selectCount(null);
        map.put("messageCount", messageCount);
        Long postCount = postService.getBaseMapper().selectCount(null);
        map.put("postCount", postCount);
        // 计算12个月前的日期
        // 获取今年的第一天和当前日期
        LocalDate firstDayOfThisYear = LocalDate.now().withDayOfYear(1);
        LocalDate today = LocalDate.now();
        QueryWrapper<Post> queryWrapper = Wrappers.query();
        queryWrapper.le("publishTime", today);
        queryWrapper.ge("publishTime", firstDayOfThisYear);  // 大于等于今年第一天
        queryWrapper.select("YEAR(publishTime) as year", "MONTH(publishTime) as month", "COUNT(*) as total");
        queryWrapper.groupBy("YEAR(publishTime)", "MONTH(publishTime)");
        queryWrapper.orderByAsc("YEAR(publishTime)", "MONTH(publishTime)");
        List<Map<String, Object>> months = postService.getBaseMapper().selectMaps(queryWrapper);
        map.put("month", months);
        QueryWrapper<Post> tagsQueryWrapper = new QueryWrapper<>();
        tagsQueryWrapper.select("tagId", "count(1) as count").groupBy("tagId").orderByDesc("count");
        List<Map<String, Object>> tags = postService.getBaseMapper().selectMaps(tagsQueryWrapper);
        List<Map<String, Object>> labelPercentages = new ArrayList<>();
        for (Map<String, Object> tag : tags) {
            double count = Double.parseDouble(tag.get("count").toString());
            double percentage = count / Double.parseDouble(map.get("postCount").toString()) * 100;
            tag.put("percentage", Double.toString(percentage).substring(0, 4));
            Tags tagName = tagsService.getById((Integer) tag.get("tagId"));
            tag.put("name", tagName.getName());
            labelPercentages.add(tag);
        }

        map.put("tag", labelPercentages.subList(0, 6));
        return JSON.toJSONString(map);
    }

    /**
     * 获取用户列表
     *
     * @param current 当前
     * @return {@link String}
     */
    public String obtainUserList(Integer current) {
        Page<User> page = page(new Page<>(current, 7));
        return JSON.toJSONString(page);
    }


    /**
     * 更改用户信息
     *
     * @param user 用户
     * @return {@link String}
     */
    public String changeUserInformation(User user) {
        boolean update = update(user, new UpdateWrapper<User>().eq("userId", user.getUserId()));
        return JSON.toJSONString(update);
    }

    /**
     * 删除用户
     *
     * @param userId 用户 ID
     * @return {@link String}
     */
    public String deleteUser(Integer userId) {
        return JSON.toJSONString(removeById(userId));
    }

    /**
     * 搜索用户
     *
     * @param username 用户名
     * @param email    电子邮件
     * @param current  当前
     * @return {@link String}
     */
    public String searchUser(String username, String email, Integer current) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!username.equals("null")) queryWrapper.like("username", username);
        if (!email.equals("null")) queryWrapper.like("email", email);
        return JSON.toJSONString(page(new Page<>(current, 7), queryWrapper));
    }

    /**
     * 制裁用户
     *
     * @param user 用户
     */
    public void sanctionUser(User user) {
        redisTemplate.opsForValue().set(user.getUserId().toString(), "", Duration.ofSeconds(Long.parseLong(user.getTemporary())));
    }

}
