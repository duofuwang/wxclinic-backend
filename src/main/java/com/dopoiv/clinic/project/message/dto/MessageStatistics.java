package com.dopoiv.clinic.project.message.dto;

import com.dopoiv.clinic.common.tools.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息数量统计
 *
 * @author doverwong
 * @date 2021/5/19 15:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MessageStatistics extends BaseEntity {

    /**
     * 日期
     */
    private String datetime;

    /**
     * 数量
     */
    private Integer num;
}
