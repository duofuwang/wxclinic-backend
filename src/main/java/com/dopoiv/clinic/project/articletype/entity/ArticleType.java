package com.dopoiv.clinic.project.articletype.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dopoiv.clinic.common.tools.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ArticleType 实体类
 *
 * @author wangduofu
 * @since 2021-05-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "ArticleType对象", description = "")
public class ArticleType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "类型")
    private String type;

    @TableField(exist = false)
    private List<ArticleType> children;

}