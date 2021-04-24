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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author doverwong
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

    @Autowired
    private RedisCache redisCache;

    @Autowired
    UserMapper userMapper;

    /**
     * 一秒钟
     */
    protected static final long MILLIS_SECOND = 1000;

    /**
     * 一分钟
     */
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    /**
     * 5天
     */
    private static final Long MILLIS_MINUTE_TEN = 5 * 24 * 60 * 60 * 1000L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StrUtil.isNotEmpty(token)) {
//            Claims claims = getClaimsFromToken(token);
//
//            LOGGER.debug("expiration: {}", getExpirationDateFromToken(token));
//            LOGGER.debug("isTokenExpired: {}", isTokenExpired(token));
//            LOGGER.debug("claims: {}", getClaimsFromToken(token));
//            LOGGER.debug("canTokenBeRefreshed: {}", canTokenBeRefreshed(token));
//            LOGGER.debug("canTokenBeRefreshed: {}", canTokenBeRefreshed(refreshToken(token)));

            // 解析对应的权限以及用户信息
//            String openid = (String) claims.get("openid");
//            LoginUser loginUser = new LoginUser();
//            User user = userMapper.selectById(openid);
//            loginUser.setUser(user);
//            loginUser.setToken(token);
//            loginUser.setExpireTime(claims.getExpiration().getTime());
//            loginUser.setLoginTime(System.currentTimeMillis());
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
     * 创建一个 token
     *
     * @param openid     用户标识
     * @param sessionKey 微信会话密钥
     * @return token
     */
    public String generateToken(String openid, String sessionKey) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireTime * MILLIS_MINUTE);
        Map<String, Object> claims = new HashMap<>();
        claims.put("openid", openid);
        claims.put("sessionKey", sessionKey);
        claims.put("sub", openid);
        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam("typ", "JWT")
                .setExpiration(expireDate)
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
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireTime * MILLIS_MINUTE);
        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam("typ", "JWT")
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 创建 token
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
     * 从 token 中获取用户 id
     *
     * @param token token
     * @return 用户 id
     */
    public String getUserIdFromToken(String token) {
        try {
            return getClaimsFromToken(token).getSubject();
        } catch (ExpiredJwtException e) {
            LOGGER.debug("token expired");
            //如果过期, 需要在此处异常调用如下的方法, 否则拿不到用户名
            return e.getClaims().getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析 token
     *
     * @param token 传入 token 字符串
     * @return Claims信息
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
     * 从 token 中获取过期时间
     *
     * @param token token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 判断 token 是否过期
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
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
     * token 是否可以被刷新(过期就可以被刷新)
     *
     * @param token token
     * @return 可以：true 不可以：false
     */
    public Boolean canTokenBeRefreshed(String token) {
        return isTokenExpired(token);
    }

    /**
     * 验证令牌有效期，如果相差不足 10 分钟，则自动刷新缓存
     *
     * @param loginUser 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        // 令牌过期时间
        long expireTime = loginUser.getExpireTime();
        // 当前时间
        long currentTime = System.currentTimeMillis();
        // 如果相差不足 10 分钟，则自动刷新缓存
        if (expireTime - currentTime <= (10 * MILLIS_MINUTE)) {
            refreshToken(loginUser);
        }
    }

    /**
     * 判断 token 是否合法
     *
     * @param token token
     * @param user  用户对象
     * @return 合法：true 不合法：false
     */
    public Boolean validateToken(String token, User user) {
        String userId = getUserIdFromToken(token);
        //如果用户名与token一致且token没有过期, 则认为合法
        return (userId.equals(user.getId()) && !isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            // 获得 Token 的 Claims, 由于在生成 JWT 的时候会根据当前时间更新过期时间, 我们只需要手动修改
            // 放在自定义属性中的创建时间就可以了
            Claims claims = getClaimsFromToken(token);
            // 利用修改后的claims再次生成token, 就不需要我们每次都去查用户的信息和权限了
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
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
        LOGGER.debug("取得 token 缓存: {}", redisCache.getCacheObject(userKey).toString());
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
