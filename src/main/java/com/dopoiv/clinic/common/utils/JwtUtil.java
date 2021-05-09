package com.dopoiv.clinic.common.utils;

import cn.hutool.core.util.StrUtil;
import com.dopoiv.clinic.common.constant.Constants;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.mapper.UserMapper;
import com.dopoiv.clinic.redis.RedisCache;
import com.dopoiv.clinic.security.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author dov
 * @date 2021/2/25 14:50
 */

@Component
public class JwtUtil {

    /**
     * 令牌自定义标识
     */
    @Value("${token.header}")
    private String header;

    /**
     * 令牌秘钥
     */
    @Value("${token.secret}")
    private String secret;

    /**
     * 令牌有效期
     */
    @Value("${token.expireTime}")
    private int expireTime;

    /**
     * 刷新阈值
     */
    @Value("${token.threshold}")
    private int threshold;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    UserMapper userMapper;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StrUtil.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            if (redisCache.hasKey(userKey)) {
                // 查找键为 login_token:token 的缓存
                return redisCache.getCacheObject(userKey);
            } else {
                // 未找到，需要重新登录
                LOGGER.error("未找到缓存: {}", userKey);
                return null;
            }
        }
        return null;
    }

    /**
     * 用户登出，删除缓存中的 token
     * @param token token信息
     */
    public void deleteLoginUser(String token) {
        if (StrUtil.isNotEmpty(token) && redisCache.hasKey(getTokenKey(token))) {
            redisCache.deleteObject(getTokenKey(token));
        }
    }

    /**
     * 创建一个 token
     *
     * @param openid     用户标识
     * @param sessionKey 微信会话密钥
     * @return token
     */
    public String generateToken(String openid, String sessionKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("openid", openid);
        claims.put("sessionKey", sessionKey);
        claims.put("sub", openid);
        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 使用 claims 创建一个 token
     *
     * @param claims claims 信息
     * @return token 字符
     */
    public String generateToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 创建 token
     *
     * @param loginUser 用户登录信息
     * @return token
     */
    public String createToken(LoginUser loginUser) {
        User user = loginUser.getUser();
        loginUser.setToken(generateToken(user.getOpenId(), user.getSessionKey()));
        refreshToken(loginUser);
        return loginUser.getToken();
    }

    /**
     * 解析 token
     *
     * @param token 传入 token 字符串
     * @return Claims 信息
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // JWT 对于处于过期时间之外的 token 不会解析, 而会抛出异常
            // 如果过期, 需要在此处异常调用如下的方法, 否则无法解析过期的 token
            return e.getClaims();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取请求 token
     *
     * @param request 请求体
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        return request.getHeader(header);
    }

    /**
     * token 是否可以被刷新 (即将过期就可以被刷新)
     *
     * @param loginUser 登录信息
     * @return 可以：true 不可以：false
     */
    public Boolean canTokenBeRefreshed(LoginUser loginUser) {
        // 过期时间
        long expireTime = loginUser.getExpireTime();
        // 当前时间
        long currentTime = System.currentTimeMillis();
        return expireTime - currentTime <= (threshold * MILLIS_MINUTE);
    }

    /**
     * 验证令牌有效期，如果相差不足 10 分钟，则自动刷新缓存
     *
     * @param loginUser 登录信息
     */
    public void verifyToken(LoginUser loginUser) {
        // 如果 token 即将到期，则自动刷新缓存中 token 的有效时间
        if (canTokenBeRefreshed(loginUser)) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新 redis 中令牌的有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        // 过期时间 {expireTime} 分钟
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);

        // 将 login_token:token 作为键值，LoginUser 作为 value 存至 redis 缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
        LOGGER.debug("更新 token 缓存 key: {}，value: {}", userKey, loginUser);
    }

    /**
     * 以 login_token:token 作为键值
     *
     * @param token token
     * @return login_token:token
     */
    public String getTokenKey(String token) {
        return Constants.LOGIN_TOKEN_KEY + token;
    }

}
