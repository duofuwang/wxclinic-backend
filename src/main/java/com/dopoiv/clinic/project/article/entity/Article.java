package com.dopoiv.clinic.project.article.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.dopoiv.clinic.common.tools.BaseEntity;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Article 实体类
 *
 * @author wangduofu
 * @since 2021-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "Article对象", description = "")
public class Article extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章摘要")
    private String summary;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "作者")
    private String userId;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "文章类型")
    private String type;

    @ApiModelProperty(value = "1发布 0草稿")
    private Integer status;

    @ApiModelProperty(value = "文章头图")
    private String banner;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    @ApiModelProperty(value = "作者名")
    @TableField(exist = false)
    private String author;
}