package com.ruoyi.framework.shiro.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@ConfigurationProperties("jwt.config")
@Component
public class JwtUtils {
    private String key;
    private long ttl;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String generateToken(long userId,String username, String password) {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        JwtBuilder builder = Jwts.builder().setId(String.valueOf(userId)).setSubject(username)
                .setIssuedAt(date)
                .signWith(SignatureAlgorithm.HS256, key).claim("role", password);
        if (ttl > 0){
            builder.setExpiration(new Date(millis + ttl));
        }
        return builder.compact();
    }

    /**
     * 解析jwt
     */
    public Claims parseJwt(String jwtStr){
        return Jwts.parser().setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}
