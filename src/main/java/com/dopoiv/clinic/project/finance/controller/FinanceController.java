package com.dopoiv.clinic.project.finance.controller;

import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.finance.service.IFinanceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author doverwong
 * @date 2021/5/17 15:25
 */
@RestController
@RequestMapping("/finance")
public class FinanceController extends BaseController {

    @Autowired
    private IFinanceService financeService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true),
            @ApiImplicitParam(name = "startDate", paramType = "query", value = "开始时间"),
            @ApiImplicitParam(name = "endDate", paramType = "query", value = "结束时间"),
            @ApiImplicitParam(name = "sortBy", paramType = "query", value = "统计方式", required = true)
    })
    @ApiOperation(value = "分页获取财务信息")
    @GetMapping("/page")
    public R page(String startDate, String endDate, String sortBy) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(financeService.getPageForQuery(pageDomain, startDate, endDate, sortBy));
    }
}
