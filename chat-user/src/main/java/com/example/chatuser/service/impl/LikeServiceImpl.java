package com.example.chatuser.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.Information;
import com.example.chatuser.entity.Like;
import com.example.chatuser.entity.Post;
import com.example.chatuser.mapper.LikeMapper;
import com.example.chatuser.service.ILikeService;
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
 * @since 2023-10-05
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements ILikeService {
    @Autowired
    private PostServiceImpl postService;

    /**
     * 按 ID 获取点赞列表
     *
     * @param userId 用户标识
     * @return {@link String}
     */
    public String getLikeListById(Integer userId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Like> likes = list(queryWrapper);
        //查询帖子内容
        ArrayList<HashMap<String, Object>> maps = new ArrayList<>();
        if (likes.size() != 0) {
            likes.forEach(like -> {
                HashMap<String, Object> hashMap = new HashMap<>();
                QueryWrapper<Post> postQueryWrapper = new QueryWrapper<>();
                postQueryWrapper.select("content").eq("postId", like.getPostId());
                Post one = postService.getOne(postQueryWrapper);
                hashMap.put("like", like);
                hashMap.put("content", one.getContent());
                maps.add(hashMap);
            });
        }
        return maps.size() == 0 ? Information.toString("error", "什么都没有") : JSON.toJSONString(maps);
    }

    /**
     * 按帖子ID设置Like
     *
     * @param like 点赞
     * @return {@link String}
     */
    public String setLikeByPostId(Like like) {
        like.setLikeTime(LocalDateTime.now());
        return JSON.toJSONString(save(like));
    }

    public String deleteLikeByUserId(Like like) {
        QueryWrapper<Like> likeQueryWrapper = new QueryWrapper<>();
        likeQueryWrapper.and(wrapper -> {
            wrapper.eq("postId", like.getPostId()).eq("userId", like.getUserId());
        });
        return JSON.toJSONString(remove(likeQueryWrapper));
    }
}
