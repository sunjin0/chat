package com.example.chatuser.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.Information;
import com.example.chatuser.entity.User;
import com.example.chatuser.entity.Userrelationship;
import com.example.chatuser.mapper.UserrelationshipMapper;
import com.example.chatuser.service.IUserrelationshipService;
import com.example.chatuser.util.ParamUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-10-14
 */
@Service
public class UserrelationshipServiceImpl extends ServiceImpl<UserrelationshipMapper, Userrelationship> implements IUserrelationshipService {
    @Autowired
    private UserServiceImpl userService;

    /**
     * 获取拼音
     *
     * @param input 输入
     * @return {@link String}
     */
    private static String getPinyin(String input) {
        StringBuilder output = new StringBuilder();
        char[] chars = input.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        for (char c : chars) {
            if (Character.isLetter(c)) {  // 判断字符是否为字母
                output.append(Character.toLowerCase(c));
            } else if (isChinese(c)) {  // 判断字符是否为中文
                String[] pinyinArray = new String[0];
                try {
                    pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
                if (pinyinArray == null) {
                    output.append(c);
                } else {
                    output.append(pinyinArray[0].charAt(0));
                }
            }
        }

        return output.toString();
    }

    /**
     * @param key      按这个字段拼音，排序分组
     * @param arrayMap {@link List}<{@link HashMap}<{@link String}, {@link Object}>>
     * @return {@link Map}<{@link String}, {@link List}<{@link HashMap}<{@link String}, {@link Object}>>>
     * @apiNote 按拼音顺序排列分组，字符串的第一个文字无论是中文还是英文，不影响排序结果，<p>应用场景: </p><p>通讯录，好友列表，A-Z。</p>
     * @since 2023-10-17
     **/
    private static Map<String, List<HashMap<String, Object>>> GroupedInPinyinOrder(ArrayList<HashMap<String, Object>> arrayMap, String key) {
        // 创建自定义的Comparator
        Comparator<HashMap<String, Object>> comparator = (hashMap, hashMap2) -> {
            String name = (String) hashMap.get(key);
            String name2 = (String) hashMap2.get(key);
            // 比较首字母或拼音
            String pinyin = getPinyin(name);
            String pinyin2 = getPinyin(name2);
            return pinyin.compareTo(pinyin2);
        };
        //按拼音顺序排序A-Z
        arrayMap.sort(comparator);
        //按拼音顺序分组A-Z
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        Map<String, List<HashMap<String, Object>>> groupedData = new TreeMap<>();
        for (HashMap<String, Object> data : arrayMap) {
            String name = (String) data.get(key);
            //作为groupedData的key
            String pinyin = String.valueOf(getPinyin(name).charAt(0)).toUpperCase();
            char c = pinyin.toLowerCase().toCharArray()[0];
            try {
                //如果首字符为中文，则转换成拼音取首字母
                if (isChinese(c)) {
                    pinyin = String.valueOf(PinyinHelper.toHanyuPinyinStringArray(c, format)[0].toUpperCase().charAt(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<HashMap<String, Object>> group = groupedData.computeIfAbsent(pinyin, k -> new ArrayList<>());
            group.add(data);
        }
        return groupedData;
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || block == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || block == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || block == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     * 添加好友
     *
     * @param userrelationship 用户关系
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public Object addFriends(Userrelationship userrelationship) {

        if (ParamUtils.isNull(userrelationship)) {
            return Information.toString("Warn", "参数异常！");
        }
        QueryWrapper<Userrelationship> queryWrapper = new QueryWrapper<>();
        Integer userId = userrelationship.getUserId();
        Integer toId = userrelationship.getToId();
        queryWrapper.and(wrapper -> wrapper.eq("userId", userId).eq("toId", toId)).or(wrapper -> wrapper.eq("userId", toId).eq("toId", userId));
        Userrelationship one = this.getOne(queryWrapper);
        if (one == null) {
            //获取用户名设置备注
            User toUser = userService.getById(userrelationship.getToId());
            User user = userService.getById(userrelationship.getUserId());
            userrelationship.setNotes(user.getUsername());
            userrelationship.setToNotes(toUser.getUsername());
            userrelationship.setTime(LocalDateTime.now());
            //保存进朋友表
            boolean save = this.save(userrelationship);
            if (!save) {
                return Information.toString("Warn", "添加失败！");
            }
        }
        if (one != null && one.getState() == 0) {
            return Information.toString("Warn", "已发送请求了！");
        }
        if (one != null && one.getState() == 1) {
            return Information.toString("Warn", "已经是好友了！");
        }

        return Information.toString("OK", "已发送");
    }

    /**
     * 通过 ID 获取好友状态
     *
     * @param id 编号
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public Object getFriendsStateById(Integer id) {
        //用户向其他用户，发送的好友请求
        ArrayList<HashMap<String, Object>> arrayMap = new ArrayList<>();
        userRelation("userId", id, arrayMap, true);
        //其他用户向用户，发送好友请求
        userRelation("toId", id, arrayMap, false);
        return JSON.toJSONString(arrayMap);
    }

    private void userRelation(String column, Integer id, ArrayList<HashMap<String, Object>> arrayMap, boolean isSelf) {
        QueryWrapper<Userrelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column, id).orderByAsc("time");
        List<Userrelationship> friends = this.list(queryWrapper);

        if (!(friends.size() == 0)) {
            friends.forEach(friend -> {
                HashMap<String, Object> hashMap = new HashMap<>();
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                Integer uid = isSelf ? friend.getToId() : friend.getUserId();
                wrapper.eq("userId", uid);
                addMap(friend, hashMap, userService.getOne(wrapper), isSelf);
                arrayMap.add(hashMap);
            });
        }
    }

    private void addMap(Userrelationship userrelationship, HashMap<String, Object> hashMap, User user, boolean isSelf) {
        if (isSelf) {
            hashMap.put("me", "me");
        }
        hashMap.put("id", user.getUserId());
        hashMap.put("username", user.getUsername());
        hashMap.put("avatar", user.getAvatar());
        hashMap.put("message", "你好");
        hashMap.put("state", userrelationship.getState());
        hashMap.put("time", userrelationship.getTime());
    }

    /**
     * 通过 ID 设置好友状态
     *
     * @param relationship 关系
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public Object setFriendsStateById(Userrelationship relationship) {
        UpdateWrapper<Userrelationship> updateWrapper = new UpdateWrapper<>();
        Integer toId = relationship.getToId();
        Integer userId = relationship.getUserId();
        updateWrapper.set("state", 1).and(wrapper -> wrapper.eq("userId", userId).eq("toId", toId)).or(wrapper -> wrapper.eq("toId", userId).eq("userId", toId));
        return this.update(updateWrapper) ? Information.toString("OK", "更改成功！") : Information.toString("Error", "出错了");
    }

    /**
     * 结交朋友
     *
     * @param userId 用户标识
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public Object getFriends(Integer userId) {
        //主动添加到好友
        QueryWrapper<Userrelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("userId", userId).eq("state", 1));
        List<Userrelationship> list = this.list(queryWrapper);
        ArrayList<HashMap<String, Object>> arrayMap = new ArrayList<>();
        if (!(list.size() == 0)) {
            list.forEach((elem -> {
                HashMap<String, Object> hashMap = new HashMap<>();
                User user = userService.getById(elem.getToId());
                hashMap.put("userId", userId);
                hashMap.put("id", elem.getToId());
                hashMap.put("avatar", user.getAvatar());
                hashMap.put("name", elem.getToNotes());
                hashMap.put("username", user.getUsername());
                hashMap.put("sex", user.getSex());
                arrayMap.add(hashMap);
            }));
        }
        //被动添加到好友
        QueryWrapper<Userrelationship> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.and(wrapper -> wrapper.eq("toId", userId).eq("state", 1));
        List<Userrelationship> list2 = this.list(queryWrapper2);
        if (!(list2.size() == 0)) {
            list2.forEach((elem -> {
                HashMap<String, Object> hashMap = new HashMap<>();
                User user = userService.getById(elem.getUserId());
                hashMap.put("userId", userId);
                hashMap.put("id", user.getUserId());
                hashMap.put("avatar", user.getAvatar());
                hashMap.put("name", elem.getNotes());
                hashMap.put("username", user.getUsername());
                hashMap.put("sex", user.getSex());
                arrayMap.add(hashMap);
            }));
        }
        return JSON.toJSONString(GroupedInPinyinOrder(arrayMap, "name"));
    }

    /**
     * 通过 ID 获取好友信息
     *
     * @param userrelationship 用户关系
     * @return {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public String getFriendsInfoById(Userrelationship userrelationship) {
        QueryWrapper<Userrelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(
                wrapper -> wrapper
                        .eq("userId", userrelationship.getUserId()).eq("toId", userrelationship.getToId())
        );
        Userrelationship one = this.getOne(queryWrapper);
        if (one != null) {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("userId", one.getToId());
            return JSON.toJSONString(userService.getOne(wrapper).setTemporary(one.getToNotes()));
        }
        //换位
        QueryWrapper<Userrelationship> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.and((wrapper2 -> wrapper2
                .eq("userId", userrelationship.getToId()).eq("toId", userrelationship.getUserId())));
        Userrelationship one1 = this.getOne(queryWrapper1);
        if (one1 != null) {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("userId", one1.getUserId());
            return JSON.toJSONString(userService.getOne(wrapper).setTemporary(one1.getNotes()));
        }
        return null;
    }

    /**
     * 变更备注
     *
     * @param userrelationship 用户关系
     * @return {@link String}
     */
    public String changeRemarks(Userrelationship userrelationship) {
        UpdateWrapper<Userrelationship> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("toNotes", userrelationship.getToNotes())
                .and(wrapper -> wrapper
                        .eq("userId", userrelationship.getUserId())
                        .eq("toId", userrelationship.getToId()));
        boolean update = update(updateWrapper);
        UpdateWrapper<Userrelationship> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.set("notes", userrelationship.getToNotes())
                .and(wrapper -> wrapper
                        .eq("userId", userrelationship.getToId())
                        .eq("toId", userrelationship.getUserId()));
        boolean update2 = update(updateWrapper2);
        return JSON.toJSONString(update || update2);
    }

    public String deleteFriend(Userrelationship userrelationship) {
        boolean remove = remove(
                query()
                        .getWrapper()
                        .and(
                                wrapper1 -> wrapper1
                                        .eq("userId", userrelationship.getUserId())
                                        .eq("toId", userrelationship.getToId())
                                        .or(wrapper -> wrapper
                                                .eq("userId", userrelationship.getToId())
                                                .eq("toId", userrelationship.getUserId())))
        );
        return JSON.toJSONString(remove);
    }


}
