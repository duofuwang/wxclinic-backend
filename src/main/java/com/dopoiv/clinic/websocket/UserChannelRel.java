package com.dopoiv.clinic.websocket;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * 用户 id和 channel 的关联关系处理
 *
 * @author doverwong
 * @date 2021/2/28 13:03
 */
public class UserChannelRel {
    private static final HashMap<String, Channel> MANAGER = new HashMap<>();

    public static void put(String senderId, Channel channel) {
        MANAGER.put(senderId, channel);
    }

    public static Channel get(String senderId) {
        return MANAGER.get(senderId);
    }

    public static void output() {
        for (HashMap.Entry<String, Channel> entry : MANAGER.entrySet()) {
            System.out.println("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }
}
