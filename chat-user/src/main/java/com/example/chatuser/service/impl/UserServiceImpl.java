package com.example.chatuser.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.Information;
import com.example.chatuser.entity.User;
import com.example.chatuser.mapper.UserMapper;
import com.example.chatuser.service.IUserService;
import com.example.chatuser.util.BCryptUtil;
import com.example.chatuser.util.JwtUtils;
import com.example.chatuser.util.MailUtils;
import com.example.chatuser.util.ParamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

/**
 * 用户服务协议
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-10-05
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private BCryptUtil bCryptUtil;
    @Autowired
    private MailUtils mailUtils;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserrelationshipServiceImpl relationshipService;

    /**
     * 注册
     *
     * @param user 中的Email
     * @return {@link String}
     * @apiNote 注册
     * @since 2022-10-11
     */
    @Transactional(rollbackFor = Exception.class)
    public String register(User user) {
        user.setRegistrationDate(LocalDateTime.now());
        log.info("验证码: {}，缓存: {},结果: {}", redisTemplate.opsForValue().get(user.getEmail()), user.getTemporary(), Objects.equals(redisTemplate.opsForValue().get(user.getEmail()), user.getTemporary()));
        //验证，验证码
        String s = Objects.equals(redisTemplate.opsForValue().get(user.getEmail()), user.getTemporary()) ? "" : Information.toString("Warn", "验证码错误");
        if (!s.equals("")) {
            return s;
        }
        //密码加密
        user.setPassword(bCryptUtil.bCryptPassword(user.getPassword()));
        boolean save = this.save(user);
        return save ? Information.toString("Info", "Successful") : Information.toString("Info", "Error");
    }

    /**
     * 获取验证码
     *
     * @param user 中的Email
     * @return {@link String}
     * @apiNote 获取验证码
     * @since 2022-10-11
     */
    @Transactional(rollbackFor = Exception.class)
    public String getCode(User user) {
        if (user.getEmail() == null) {
            return Information.toString("Warn", "邮箱为空！");
        }
        //        sql查询构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("email");
        queryWrapper.eq("email", user.getEmail());
        User one = this.getOne(queryWrapper);
        log.info(String.valueOf(user));
        if (one != null) {
            return Information.toString("Warn", "邮箱已存在！");
        }
        boolean sendMail = mailUtils.sendMail(user.getEmail(), "校园趣事");
        if (!sendMail) {
            return Information.toString("Warn", "验证码发送失败！");
        }
        return Information.toString("Successful", "验证码发送成功！");
    }

    /**
     * 登录
     *
     * @param user 用户信息
     * @return {@link String}
     * @since 2023-10-19
     */
    @Transactional(rollbackFor = Exception.class)
    public String login(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail().trim());
        User one = this.getOne(queryWrapper);
        boolean isNull = Objects.isNull(one);
        if (isNull) {
            return Information.toString("Warn", "该账号没有注册！");
        }
        if (!bCryptUtil.matches(user.getPassword(), one.getPassword())) {
            return Information.toString("Warn", "密码错误！");
        }
        String token = jwtUtils.generateToken(one.getUserId().toString());
        //携带token
        one.setTemporary(token);
        //放进redis设置30分钟
        redisTemplate.opsForValue().set(token, token, Duration.ofMinutes(30));
        log.info(user.setPassword("").toString());
        return JSON.toJSONString(one.setPassword(""));
    }

    /**
     * 搜索用户
     *
     * @param user 用户信息
     * @return {@link Object}
     * @apiNote 使用user的id或者Email查询好友
     */
    @Transactional(rollbackFor = Exception.class)
    public Object searchUser(User user) {
        Object idOrEmail = user.getEmail();
        if (ParamUtils.isNull(idOrEmail) || ParamUtils.isBlank(idOrEmail.toString())) {
            return Information.toString("Warn", "参数异常！");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //查询用户信息并不查询密码
        queryWrapper.select(User.class, info -> !info.getColumn().equals("password")).eq("userId", idOrEmail).or().eq("email", idOrEmail);
        //抛出异常
        User one = this.getOne(queryWrapper);
        if (ParamUtils.isNull(one)) {
            return Information.toString("Info", "没有该用户！");
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", one.getUserId());
        hashMap.put("username", one.getUsername());
        hashMap.put("avatar", one.getAvatar());
        hashMap.put("state", 0);
        return JSON.toJSONString(hashMap);
    }

    /**
     * 按 ID 获取用户信息
     *
     * @param id 编号
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public String getUserInfoById(Integer id) {
        User user = this.getById(id);
        return JSON.toJSONString(user.setPassword("").setTemporary(user.getUsername()));
    }

    /**
     * 按 ID 更新用户信息
     *
     * @param user 用户
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public Object updateUserInfoById(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("username", user.getUsername()).set("bio", user.getBio()).eq("userId", user.getUserId());
        boolean b = update(updateWrapper);
        return b ? Information.toString("info", "successful") : Information.toString("error", "出错了");
    }


    public String changePassword(User user) {
        String o = (String) redisTemplate.opsForValue().get(user.getEmail());
        if (!(o != null && o.equals(user.getTemporary()))) {
            return Information.toString("error", "验证码错误");
        }
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.set("password", bCryptUtil.bCryptPassword(user.getPassword())).eq("email", user.getEmail());
        return JSON.toJSONString(update(wrapper));
    }

}
