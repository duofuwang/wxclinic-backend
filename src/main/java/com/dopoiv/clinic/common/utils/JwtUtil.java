package com.dopoiv.clinic.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author doverwong
 * @date 2021/2/25 14:50
 */

@Component
public class JwtUtil {

    /**
     * 过期时间 6 小时
     */
    private static final long EXPIRE = 60 * 60 * 1000 * 6;

    /**
     * 密钥
     */
    private static final String SECRET = "UQmqAUhUrpD";

    /**
     * 创建一个 token
     *
     * @param openid     用户标识
     * @param sessionKey 微信会话密钥
     * @return token
     */
    public String generateToken(String openid, String sessionKey) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRE);
        Map<String, Object> claims = new HashMap<>();
        claims.put("openid", openid);
        claims.put("sessionKey", sessionKey);
        return Jwts.builder().setClaims(claims).setHeaderParam("type", "JWT").setSubject(openid).setIssuedAt(now)
                .setExpiration(expireDate).signWith(
                        SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
     * 解析 token
     *
     * @param token 传入 token 字符串
     * @return Claims信息
     */
    public Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            System.out.println("validate is token error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断 token 是否过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

}
