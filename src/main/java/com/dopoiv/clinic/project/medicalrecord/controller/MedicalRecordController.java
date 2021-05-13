package com.dopoiv.clinic.project.medicalrecord.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.medicalrecord.service.IMedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.medicalrecord.mapper.MedicalRecordMapper;
import com.dopoiv.clinic.project.medicalrecord.entity.MedicalRecord;

import com.dopoiv.clinic.common.tools.BaseController;

/**
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-12
 */
@RestController
@RequestMapping("/medical-record")
public class MedicalRecordController extends BaseController {
    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private IMedicalRecordService medicalRecordService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取MedicalRecord信息")
    @GetMapping("/page")
    public R page(MedicalRecord params, String startDate, String endDate) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(medicalRecordService.getPageForQuery(pageDomain, params, startDate, endDate));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部MedicalRecord信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        MedicalRecord params = new MedicalRecord();
        QueryWrapper<MedicalRecord> wrapper = new QueryWrapper<>(params);
        List<MedicalRecord> medicalRecordList = medicalRecordMapper.selectList(wrapper);

        return R.data(medicalRecordList);
    }

    @ApiOperation(value = "保存修改MedicalRecord信息")
    @PutMapping("/save")
    public R save(@RequestBody MedicalRecord entity) {
        if (entity.getId() == null) {
            medicalRecordMapper.insert(entity);
        } else {
            medicalRecordMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除MedicalRecord，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        medicalRecordMapper.deleteBatchIds(deleteIds);
        return R.success();
    }
}
