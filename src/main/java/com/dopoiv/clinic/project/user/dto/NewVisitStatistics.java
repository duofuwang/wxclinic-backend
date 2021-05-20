package com.dopoiv.clinic.project.user.dto;

import com.dopoiv.clinic.common.tools.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 新用户统计
 *
 * @author doverwong
 * @date 2021/5/19 18:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class NewVisitStatistics extends BaseEntity {

    /**
     * 日期
     */
    private String datetime;

    /**
     * 数量
     */
    private Integer num;
}
