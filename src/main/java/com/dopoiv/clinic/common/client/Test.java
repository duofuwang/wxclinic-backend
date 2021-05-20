package com.dopoiv.clinic.common.client;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Dict;
import com.dopoiv.clinic.project.user.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author dov
 * @date 2021/3/28 22:50
 */
public class Test {
    public static void main(String[] args) {
        String createTime = "2021-03-03 15:32:19";
        String expirationTime = "2021-03-03 16:02:19";
        LocalDateTime create = LocalDateTimeUtil.parse(createTime, "yyyy-MM-dd HH:mm:ss");
        LocalDateTime expired = LocalDateTimeUtil.parse(expirationTime, "yyyy-MM-dd HH:mm:ss");
        Duration duration = LocalDateTimeUtil.between(LocalDateTime.now(), expired);
        Duration duration2 = LocalDateTimeUtil.between(create, expired);
        System.out.println(duration.toMinutes() < 0);
        System.out.println(duration2.toMinutes() > 0);
    }
}
