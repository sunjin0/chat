package com.example.chatuser.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.Follow;
import com.example.chatuser.mapper.FollowMapper;
import com.example.chatuser.service.IFollowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-11-02
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {


    /**
     * 设置关注 BY 被关注人ID和用户ID
     *
     * @param follow 跟随
     * @return {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public String setFollowing(Follow follow) {
        follow.setCreateTime(LocalDateTime.now());
        return JSON.toJSONString(save(follow));
    }


    @Transactional(rollbackFor = Exception.class)
    public String cancelFollowing(Follow follow) {
        QueryWrapper<Follow> followQueryWrapper = new QueryWrapper<>();
        followQueryWrapper
                .and(wrapper -> wrapper
                        .eq("userId", follow.getUserId())
                        .eq("followingId", follow.getFollowingId()));
        return JSON.toJSONString(remove(followQueryWrapper));
    }
}
