package com.dopoiv.clinic.common.web.page;

import com.dopoiv.clinic.common.utils.StringUtil;

/**
 * 页面域
 * 分页数据
 *
 * @author ywgy
 * @date 2021/05/19
 */
public class PageDomain {
    /**
     * 当前记录起始索引
     */
    private Integer pageNum;
    /**
     * 每页显示记录数
     */
    private Integer pageSize;
    /**
     * 排序列
     */
    private String orderByColumn;
    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    private String isAsc;

    public String getOrderBy() {
        if (StringUtil.isEmpty(orderByColumn)) {
            return "";
        }
        return StringUtil.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public String getASOrderBy() {
        if (StringUtil.isEmpty(orderByColumn)) {
            return "";
        }
        return orderByColumn + " " + isAsc;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc() {
        return isAsc;
    }

    public void setIsAsc(String isAsc) {
        this.isAsc = isAsc;
    }
}
