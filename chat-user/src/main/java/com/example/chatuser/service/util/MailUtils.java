package com.example.chatuser.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Properties;

/**
 * 发邮件工具类
 */
@Component
@PropertySource("classpath:application.data.properties")
public final class MailUtils {
    @Value("${mail.user}")
    private String USER; // 发件人邮箱地址
    @Value("${mail.password}")
    private String PASSWORD; // 如果是qq邮箱可以使客户端授权码
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 发送邮件
     *
     * @param toeMail 收件人邮箱
     * @param title   标题
     */
    public boolean sendMail(String toeMail, String title) {
        try {
            final Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", "smtp.qq.com");

            // 发件人的账号
            props.put("mail.user", USER);
            //发件人的密码
            props.put("mail.password", PASSWORD);

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    return new PasswordAuthentication(USER, PASSWORD);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress form = new InternetAddress(USER);
            message.setFrom(form);

            // 设置收件人
            InternetAddress toAddress = new InternetAddress(toeMail);
            message.setRecipient(Message.RecipientType.TO, toAddress);

            // 设置邮件标题
            message.setSubject(title);
            // 生成随机的6位数字验证码
            int code = (int) (Math.random() * 900000 + 100000);
            //放入redis
            redisTemplate.opsForValue().set(toeMail, code, Duration.ofMinutes(5));
            // 设置邮件的内容体
            message.setContent("你的验证码为:  " + code + "有效时间5分钟！", "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
