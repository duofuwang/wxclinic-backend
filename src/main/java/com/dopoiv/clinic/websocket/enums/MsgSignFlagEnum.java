package com.dopoiv.clinic.websocket.enums;

/**
 * @author doverwong
 * @date 2021/2/28 12:49
 */
public enum MsgSignFlagEnum {
    UNSIGN(0, "未读"),
    SIGNED(1, "已读");

    public final Integer type;
    public final String content;


    MsgSignFlagEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }
}
