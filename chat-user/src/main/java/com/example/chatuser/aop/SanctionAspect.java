package com.example.chatuser.aop;

import com.example.chatuser.entity.Information;
import com.example.chatuser.entity.Post;
import com.example.chatuser.service.util.TimeConversionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class SanctionAspect {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.example.chatuser.annotation.Sanction)")
    public void Pointcut() {

    }

    @Around("Pointcut()")
    public String around(ProceedingJoinPoint pjp) throws Throwable {
        Post post = (Post) pjp.getArgs()[0];
        Long aLong = redisTemplate.opsForValue().getOperations().getExpire(post.getUserId().toString());
        if (aLong != null && aLong.intValue() != -2) return Information.
                toString("error",
                        "由于你发布的内容多次违规，限制你"
                                + TimeConversionUtil.convertSecondsToTime(aLong)
                                + "内不能发布帖子。");
        pjp.proceed();//执行目标方法
        return null;


    }
}
