package com.dopoiv.clinic.common.tools;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.common.web.page.TableSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础Controller
 *
 * @author dov
 */
@Transactional
public class BaseController {

    protected PageDomain startMybatisPlusPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (ObjectUtil.isNotNull(pageNum) && ObjectUtil.isNotNull(pageSize)) {
            return pageDomain;
        }
        pageDomain.setPageNum(1);
        pageDomain.setPageSize(10);
        return pageDomain;
    }
}
