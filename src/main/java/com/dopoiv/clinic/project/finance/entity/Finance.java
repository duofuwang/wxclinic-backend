package com.dopoiv.clinic.project.finance.entity;

import lombok.Data;

/**
 * 财务信息
 *
 * @author doverwong
 * @date 2021/5/17 15:27
 */
@Data
public class Finance {

    /**
     * 时间
     */
    private String datetime;


    /**
     * 总费用
     */
    private String totalFee;


    /**
     * 实际收到的
     */
    private String actualReceived;
}
