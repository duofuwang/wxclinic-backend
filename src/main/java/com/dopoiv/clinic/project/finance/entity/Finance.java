package com.dopoiv.clinic.project.finance.entity;

import com.dopoiv.clinic.common.tools.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 财务信息
 *
 * @author doverwong
 * @date 2021/5/17 15:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Finance extends BaseEntity {

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


