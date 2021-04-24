package com.dopoiv.clinic.common.client;

import cn.hutool.core.lang.Dict;
import com.dopoiv.clinic.project.user.entity.User;

/**
 * @author doverwong
 * @date 2021/3/28 22:50
 */
public class Test {
    public static void main(String[] args) {
        User user = new User();
        user.setNickName("Mary").setPhoneNumber("17605941026");
        Dict dict = Dict.create().set("user", user);
        System.out.println(dict.toString());
    }
}
