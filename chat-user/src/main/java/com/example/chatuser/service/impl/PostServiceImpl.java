package com.example.chatuser.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.*;
import com.example.chatuser.mapper.PostMapper;
import com.example.chatuser.service.IPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 邮政服务IMPL
 *
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @date 2023/10/27
 * @since 2023-10-05
 */
@Slf4j
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {
    @Autowired
    private LikeServiceImpl likeService;
    @Autowired
    private TagsServiceImpl tagsService;

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentReplyServiceImpl commentReplyService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FollowServiceImpl followService;

    @Autowired
    private BrowsinghistoryServiceImpl browsinghistoryService;


    /**
     * 添加帖子
     *
     * @param post 发布信息载体
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public Object addPost(Post post) {
        return save(post) && tagsService.setTagsById(post.getTagId()) ? Information.toString("info", "发布成功！") : Information.toString("error", "失败了！");
    }

    /**
     * 按 ID 获取帖子列表
     *
     * @param userId 用户标识
     * @return {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public String getPostListById(Integer userId) {
        ArrayList<HashMap<String, Object>> mapList = new ArrayList<>();
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Post> posts = list(queryWrapper);
        if (posts == null) {
            return Information.toString("info", "什么都没有！");
        }
        posts.forEach(post -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            addHashMap(hashMap, post);
            mapList.add(hashMap);
        });
        return JSON.toJSONString(mapList);
    }

    /**
     * 热度排名
     *
     * @param arrayList 数组列表
     * @return {@link ArrayList}<{@link Integer}>
     */
    private String heatRanking(ArrayList<Integer> arrayList) {

        return "new ArrayList<>()";
    }


    /**
     * @param hashMap 封装信息载体
     * @param post    帖子信息
     * @param userId  用户标识
     * @apiNote 帖子展示页面数据封装
     */
    private void posts(HashMap<String, Object> hashMap, Post post, Integer userId) {
        addHashMap(hashMap, post);
        //查询发帖人用户信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("username", "avatar").eq("userId", post.getUserId());
        User user = userService.getOne(userQueryWrapper);
        hashMap.put("username", user.getUsername());
        hashMap.put("avatar", user.getAvatar());
        //查询发帖人关注人数
        QueryWrapper<Follow> countQueryWrapper = new QueryWrapper<>();
        countQueryWrapper.eq("followingId", post.getUserId());
        Long count = followService.getBaseMapper().selectCount(countQueryWrapper);
        hashMap.put("followCount", count);
        if (userId != null) {
            //查询当前用户是否点赞这个帖子
            QueryWrapper<Like> likeQueryWrapper = new QueryWrapper<>();
            likeQueryWrapper.and(wrapper -> wrapper.eq("userId", userId).eq("postId", post.getPostId()));
            Like one = likeService.getOne(likeQueryWrapper);
            if (one == null) {
                hashMap.put("isLike", false);
            } else {
                hashMap.put("isLike", true);

            }
            //查询当前用户是否关注这个博主
            QueryWrapper<Follow> followQueryWrapper = new QueryWrapper<>();
            followQueryWrapper.and(wrapper -> wrapper.eq("userId", userId).eq("followingId", post.getUserId()));
            Follow follow = followService.getOne(followQueryWrapper);
            if (follow == null) {
                hashMap.put("isFollow", false);
            } else {
                hashMap.put("isFollow", true);
            }

        }
    }

    /**
     * @param hashMap 封装信息载体
     * @param post    帖子信息
     * @apiNote 我的发布页面，参数封装
     */
    private void addHashMap(HashMap<String, Object> hashMap, Post post) {
        //帖子信息
        hashMap.put("post", post);
        // 查询主评论和子评论的数量
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("postId", post.getPostId());

        List<Comment> comments = commentService.list(wrapper);
        Long commentCount = (long) comments.size();   // 主评论数量

        // 计算子评论数量
        Long replyCount = 0L;
        for (Comment comment : comments) {
            QueryWrapper<CommentReply> replyWrapper = new QueryWrapper<>();
            replyWrapper.eq("commentId", comment.getCommentId());
            replyCount += commentReplyService.getBaseMapper().selectCount(replyWrapper);
        }

        // 返回评论和回复的总数
        hashMap.put("comment", commentCount + replyCount);

        //查询喜欢数
        QueryWrapper<Like> likeWrapper = new QueryWrapper<>();
        likeWrapper.eq("postId", post.getPostId());
        Long likeCount = likeService.getBaseMapper().selectCount(likeWrapper);
        hashMap.put("like", likeCount);
        //查询标签
        Tags tags = tagsService.getById(post.getTagId());
        String tag = "";
        if (tags != null) {
            tag = tags.getName();
        }
        hashMap.put("tag", tag);
        //查询帖子浏览量
        QueryWrapper<Browsinghistory> historyWrapper = new QueryWrapper<>();
        historyWrapper.eq("postId", post.getPostId());
        Long count = browsinghistoryService.getBaseMapper().selectCount(historyWrapper);
        hashMap.put("historys", count);
    }

    /**
     * 按 ID 获取帖子
     *
     * @param postId 帖子 ID
     * @return {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public String getPostById(Integer postId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("postId", postId);
        Post post = getOne(queryWrapper);
        if (post == null) {
            return Information.toString("info", "什么都没有！");
        }
        posts(hashMap, post, null);
        return JSON.toJSONString(hashMap);
    }

    /**
     * 推荐帖子
     *
     * @param userId 用户标识
     * @return {@link Object}
     */
    public String recommendPost(Long userId) {
        // 获取500个用户的浏览历史记录
        Page<Browsinghistory> page = new Page<>(1, 500);
        QueryWrapper<Browsinghistory> browsingQuery = new QueryWrapper<>();
        browsingQuery.select("userId", "postId").orderByAsc("RAND()");
        List<Browsinghistory> allHistory = browsinghistoryService.page(page, browsingQuery).getRecords();

        // 构建用户兴趣程度矩阵
        Map<Long, Map<Long, Double>> userInterestMap = new HashMap<>();
        for (Browsinghistory bh : allHistory) {
            Long uId = Long.valueOf(bh.getUserId());
            Long pId = Long.valueOf(bh.getPostId());
            //key为用户id，value为帖子map，key为帖子id，value为评分
            userInterestMap.computeIfAbsent(uId, k -> new HashMap<>()).put(pId, 1.0);
        }
        // 根据用户历史行为计算推荐结果
        Set<Integer> recommendations = calculateRecommendations(userId, userInterestMap, allHistory);
        ArrayList<HashMap<String, Object>> hashMaps = new ArrayList<>();
        List<Post> posts = listByIds(recommendations);
        posts.forEach(post -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            //封装参数
            posts(hashMap, post, Math.toIntExact(userId));
            hashMaps.add(hashMap);
        });
        return JSON.toJSONString(hashMaps);
    }


    /**
     * 计算建议
     *
     * @param userId          用户 ID
     * @param userInterestMap 用户兴趣图
     * @param allHistory      所有历史
     * @return {@link List}<{@link Long}>
     */
    private Set<Integer> calculateRecommendations(Long userId,
                                                  Map<Long, Map<Long, Double>> userInterestMap,
                                                  List<Browsinghistory> allHistory
    ) {
        // 找到目标用户没有看过的所有帖子
        List<Browsinghistory> browsingHistoryList = allHistory
                .stream()
                .filter(bh -> !bh.getUserId().toString().equals(userId.toString()))
                .collect(Collectors.toList());
        //用户相似度map，key用户id，value为相似度
        Map<Long, Map<Long, Double>> hashMap = new HashMap<>();
        for (Browsinghistory bh : allHistory) {
            Long uId = Long.valueOf(bh.getUserId());
            if (uId.equals(userId)) continue;
            //用户相似度
            double similarity = calculateSimilarity(userId, uId, userInterestMap);
            hashMap.computeIfAbsent(userId, k -> new HashMap<>()).put(uId, similarity);
        }
        // 对外层Map进行排序
        hashMap.forEach((key, value) -> {
            Map<Long, Double> sortedInnerMap = value.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(10)  // 取出前10个元素
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            hashMap.put(key, sortedInnerMap);
        });
        //用户相似度排行前10的用户id
        List<Integer> otherUserIds = Arrays.asList(hashMap.values().stream()
                .flatMap(m -> m.keySet().stream())
                .map(Long::intValue)
                .toArray(Integer[]::new));
        log.error("目标用户与其他用户相似度" + hashMap);
        // 对预测值进行排序，选择前几个推荐给目标用户
        Set<Integer> postIds = new LinkedHashSet<>();
        browsingHistoryList.forEach(history -> {
            if (otherUserIds.contains(history.getUserId())) {
                if (postIds.size() != 10) {
                    postIds.add(history.getPostId());
                }
            }
        });
        if (postIds.size() != 10) {
            int i = 10 - postIds.size();
            postIds.addAll(page(new Page<>(1, i), query().
                    getWrapper()
                    .orderByDesc("publishTime"))
                    .getRecords().stream()
                    .map(Post::getPostId)
                    .collect(Collectors.toList()));
        }
        log.error("选择前几个推荐给目标用户" + postIds);

        return postIds;
    }

    /**
     * 计算相似度
     *
     * @param userId1         用户 ID1
     * @param userId2         用户 ID2
     * @param userInterestMap 用户兴趣图
     * @return double
     */
    private double calculateSimilarity(Long userId1, Long userId2, Map<Long, Map<Long, Double>> userInterestMap) {
        Map<Long, Double> interestMap1 = userInterestMap.getOrDefault(userId1, Collections.emptyMap());
        Map<Long, Double> interestMap2 = userInterestMap.getOrDefault(userId2, Collections.emptyMap());

        // 计算余弦相似度
        double commonCount = 0;
        double count1 = 0;
        double count2 = 0;
        for (Map.Entry<Long, Double> interestEntry : interestMap1.entrySet()) {
            Long pId = interestEntry.getKey();
            if (interestMap2.containsKey(pId)) {
                commonCount += interestEntry.getValue() * interestMap2.get(pId);
            }
            count1 += Math.pow(interestEntry.getValue(), 2);
        }
        for (Map.Entry<Long, Double> interestEntry : interestMap2.entrySet()) {
            count2 += Math.pow(interestEntry.getValue(), 2);
        }
        return commonCount / (Math.sqrt(count1) * Math.sqrt(count2));
    }


    /**
     * 根据标签推荐
     *
     * @param current    当前页码
     * @param TagIdArray 标记 ID 数组
     * @return {@link String}
     */
    public String recommendedBasedOnLabels(List<Integer> TagIdArray, Integer current, Integer userId) {
        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<>();
        //随机获取与标签相关的帖子
        postQueryWrapper.in("tagId", TagIdArray).orderByAsc("RAND()");
        Page<Post> page = new Page<>(current, 10);

        Page<Post> postPage = page(page, postQueryWrapper);
        if (postPage.getRecords().isEmpty()) {
            return Information.toString("info", "什么都没有");
        }
        ArrayList<HashMap<String, Object>> hashMaps = new ArrayList<>();
        postPage.getRecords().forEach(post -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            posts(hashMap, post, userId);
            hashMaps.add(hashMap);
        });
        hashMaps.sort((map1, map2) -> {
            // 获得键 "like" 的值并进行比较
            Long like1 = (Long) map1.get("like");
            Long like2 = (Long) map2.get("like");
            int compare = Integer.compare(Math.toIntExact(like1), Math.toIntExact(like2));
            if (compare == 0) {
                Long comment1 = (Long) map1.get("comment");
                Long comment2 = (Long) map2.get("comment");
                compare = Integer.compare(Math.toIntExact(comment1), Math.toIntExact(comment2));
            }
            if (compare == 0) {
                Long historys1 = (Long) map1.get("historys");
                Long historys2 = (Long) map2.get("historys");
                compare = Integer.compare(Math.toIntExact(historys1), Math.toIntExact(historys2));
            }
            return compare;
        });
        return JSON.toJSONString(hashMaps);
    }


    /**
     * 新用户与否
     *
     * @param userId 用户 ID
     * @return boolean
     */
    public boolean newUserOrNot(Integer userId) {
        QueryWrapper<Browsinghistory> historyQueryWrapper = new QueryWrapper<>();
        historyQueryWrapper.eq("userId", userId);
        Long count = browsinghistoryService.getBaseMapper().selectCount(historyQueryWrapper);
        return count < 50;
    }

    /**
     * 根据帖子 ID 删除帖子
     *
     * @param postId 帖子 ID
     * @return {@link String}
     */
    public String deletePostByPostId(Integer postId) {
        return removeById(postId) ? Information.toString("info", "删除成功！") : Information.toString("info", "出错了！");
    }

    /**
     * 模糊查询帖子
     *
     * @param code 法典
     * @return {@link String}
     */
    public String fuzzyQueryPost(String code) {
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.like("content", code);
        Page<Post> page = page(new Page<>(1, 10), wrapper);
        return JSON.toJSONString(page.getRecords());
    }

    /**
     * 遵循建议
     *
     * @param userId 用户 ID
     * @return {@link String}
     */
    public String followRecommendations(Integer userId) {
        List<Follow> follows = followService.list(new QueryWrapper<Follow>().select("followingId").eq("userId", userId));
        List<Integer> followIds = follows.stream().map(Follow::getFollowingId).collect(Collectors.toList());
        Page<Post> posts = page(new Page<>(1, 10), new QueryWrapper<Post>().in("userId", followIds).orderByAsc("RAND()"));
        ArrayList<HashMap<String, Object>> hashMaps = new ArrayList<>();
        posts.getRecords().forEach(post -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            posts(hashMap, post, Math.toIntExact(userId));
            hashMaps.add(hashMap);
        });
        return JSON.toJSONString(hashMaps);
    }


}
