package com.example.chatuser.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.Comment;
import com.example.chatuser.entity.CommentReply;
import com.example.chatuser.entity.User;
import com.example.chatuser.mapper.CommentMapper;
import com.example.chatuser.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CommentReplyServiceImpl commentReplyService;

    /**
     * 通过帖子 ID 获取评论
     *
     * @param postId  帖子 ID
     * @param current 当前
     * @return {@link String}
     */
    public String getCommentByPostId(Integer postId, Integer current) {
        Page<Comment> commentPage = new Page<>(current, 10);
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("postId", postId);
        Page<Comment> page = page(commentPage, commentQueryWrapper);
        ArrayList<HashMap<String, Object>> maps = new ArrayList<>();
        page.getRecords().forEach(comment -> {
            HashMap<String, Object> map = new HashMap<>();
            List<CommentReply> replys = commentReplyService.getCommentReply(comment.getCommentId());

            ArrayList<HashMap<String, Object>> replyList = new ArrayList<>();
            if (!replys.isEmpty()) {
                replys.forEach(reply -> {
                    HashMap<String, Object> replyMap = new HashMap<>();
                    replyMap.put("reply", reply);
                    User user = userService.getOne(
                            new QueryWrapper<User>()
                                    .select("username", "avatar")
                                    .eq("userId", reply.getUserId())
                    );
                    replyMap.put("username", user.getUsername());
                    replyMap.put("avatar", user.getAvatar());
                    replyList.add(replyMap);
                });
                map.put("reply", replyList);
            } else {
                map.put("reply", replys);
            }

            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.select("username", "avatar").eq("userId", comment.getUserId());
            User user = userService.getOne(userQueryWrapper);
            map.put("username", user.getUsername());
            map.put("avatar", user.getAvatar());
            map.put("comment", comment);
            maps.add(map);
        });
        return JSON.toJSONString(maps);
    }

    /**
     * 按帖子 ID 设置评论
     *
     * @param comment 评论
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public String setCommentByPostId(Comment comment) {
        comment.setCommentTime(LocalDateTime.now());
        return JSON.toJSONString(save(comment));
    }
}
