package com.example.chatuser.service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:application.data.properties")
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    //生成 JWT 令牌
    public String generateToken(String userId) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 3600000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //从令牌中获取载荷信息
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //验证令牌是否合法
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //从令牌中获取用户ID
    public String getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return (String) claims.get("userId");
    }
}
