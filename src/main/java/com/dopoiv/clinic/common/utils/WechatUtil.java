package com.dopoiv.clinic.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dopoiv.clinic.common.client.MyHttpClient;
import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具类
 *
 * @author doverwong
 * @date 2021/2/24 20:53
 */
@Component
public class WechatUtil {

    /**
     * http客户端
     */
    private static MyHttpClient httpClient;

    private static String appid;
    private static String secret;

    @Autowired
    private MyHttpClient myHttpClient;

    @Value("${wx.mini.appid}")
    private String appidTemp;

    @Value("${wx.mini.secret}")
    private String secretTemp;

    @PostConstruct
    public void init() {
        httpClient = myHttpClient;
        appid = appidTemp;
        secret = secretTemp;
    }

    /**
     * 获得sessionKey和openid
     *
     * @param code 登录code
     * @return {@link JSONObject}
     */
    public static JSONObject getSessionKeyAndOpenId(String code) {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> requestUrlParam = new HashMap<>();
        // 小程序appId
        requestUrlParam.put("appid", appid);
        // 小程序secret
        requestUrlParam.put("secret", secret);
        // 小程序端返回的code
        requestUrlParam.put("js_code", code);
        // 默认参数
        requestUrlParam.put("grant_type", "authorization_code");
        // 发送post请求读取调用微信接口获取openid用户唯一标识
        return JSON.parseObject(httpClient.doGet(requestUrl, requestUrlParam));
    }

    /**
     * AES解密
     *
     * @param data 密文，被加密的数据
     * @param key  秘钥
     * @param iv   偏移量
     * @return 解密后的信息
     */
    public static String decrypt(String data, String key, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(data);
        // 加密秘钥
        byte[] keyByte = Base64.decode(key);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivByte);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(keyByte, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return new String(cipher.doFinal(dataByte), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
