package com.dopoiv.clinic.common.web.page;

import com.dopoiv.clinic.common.utils.ServletUtil;

/**
 * 表格数据处理
 *
 * @author ywgy
 */
public class TableSupport {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(ServletUtil.getParameterToInt(PAGE_NUM));
        pageDomain.setPageSize(ServletUtil.getParameterToInt(PAGE_SIZE));
        pageDomain.setOrderByColumn(ServletUtil.getParameter(ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtil.getParameter(IS_ASC));
        return pageDomain;
    }

    public static PageDomain buildPageRequest() {
        return getPageDomain();
    }
}
