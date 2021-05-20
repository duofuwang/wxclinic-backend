package com.dopoiv.clinic.project.emergency.dto;

import com.dopoiv.clinic.common.tools.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 呼救统计
 *
 * @author doverwong
 * @date 2021/5/19 18:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EmergencyStatistics extends BaseEntity {

    /**
     * 日期
     */
    private String datetime;

    /**
     * 数量
     */
    private Integer num;
}
