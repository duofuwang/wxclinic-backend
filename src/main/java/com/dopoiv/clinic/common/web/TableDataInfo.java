package com.dopoiv.clinic.common.web;

import java.io.Serializable;
import java.util.List;

/**
 * @author doverwong
 * @date 2021/4/23 20:26
 */
public class TableDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<?> rows;


    /**
     * 表格数据对象
     */
    public TableDataInfo() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, long total) {
        this.rows = list;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    long getPages(int pageSize) {
        if (pageSize == 0) {
            return 0L;
        }
        long pages = getTotal() / pageSize;
        if (getTotal() % pageSize != 0) {
            pages++;
        }
        return pages;
    }

}
