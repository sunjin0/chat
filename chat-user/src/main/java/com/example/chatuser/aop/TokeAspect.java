package com.example.chatuser.aop;

import com.example.chatuser.entity.Information;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

@Aspect
@Slf4j
@Component
public class TokeAspect {
    private static final HashMap<String, Boolean> hashMap;

    static {
        hashMap = new HashMap<>();
        hashMap.put("register", true);
        hashMap.put("getCode", true);
        hashMap.put("registerUpload", true);
        hashMap.put("changePassword", true);
        hashMap.put("upLoadVideo", true);
        hashMap.put("upLoadVoice", true);
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @apiNote Temporary参数携带toke
     */
    @Around("execution(* com.example.chatuser.controller.*.*(..))")
    public Object interceptRequest(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader("Authorization");
        String[] strings = pjp.getArgs()[0].getClass().getTypeName().split("\\.");
        log.info("请求参数：" + Arrays.toString(pjp.getArgs()));
        log.info("请求参数类型：" + strings[strings.length - 1]);
        log.info("请求方法：" + pjp.getSignature().getName());
        //获取方法名称,如果是注册则放行
        String name = pjp.getSignature().getName();
        if (hashMap.getOrDefault(name, false)) {
            return pjp.proceed();
        }
        if (token.split("\\.").length == 1) {
            return pjp.proceed();
        }
        //分割组装toke
        String[] split = token.split("\\.");
        log.error(Arrays.toString(split));
        token = split[1] + "." + split[2] + "." + split[3];
        return checkingToken(token, pjp);
    }

    private Object checkingToken(String tokens, ProceedingJoinPoint point) throws Throwable {
        // 对请求进行处理，例如验证请求参数、记录请求日志等
        String token = (String) redisTemplate.opsForValue().get(tokens);
        if (token == null) {
            return Information.toString("Warn", "登入已过期...");
        }
        if (Objects.equals(tokens, token)) {
            //toke没有过期，就重新刷新时间
            redisTemplate.opsForValue().set(token, token, Duration.ofMinutes(24 * 60 * 3));
            //执行目标方法
            if (!point.getSignature().getName().equals("login")) {
                return point.proceed();
            }
        }

        return null;
    }

}
