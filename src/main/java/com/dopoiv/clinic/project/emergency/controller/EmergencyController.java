package com.dopoiv.clinic.project.emergency.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.emergency.entity.Emergency;
import com.dopoiv.clinic.project.emergency.mapper.EmergencyMapper;
import com.dopoiv.clinic.project.emergency.service.IEmergencyService;
import com.dopoiv.clinic.project.emergency.vo.UserEmergencyVo;
import com.dopoiv.clinic.websocket.handler.WebsocketMessageHandler;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author duofuwang
 * @since 2021-04-10
 */
@RestController
@RequestMapping("/emergency")
public class EmergencyController extends BaseController {
    @Autowired
    private EmergencyMapper emergencyMapper;

    @Autowired
    private IEmergencyService emergencyService;

    @Autowired
    private WebsocketMessageHandler websocketMessageHandler;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Emergency信息")
    @GetMapping("/page")
    public R page(UserEmergencyVo params, String startDate, String endDate) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(emergencyService.pageForQuery(pageDomain, params, startDate, endDate));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Emergency信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Emergency params = new Emergency();
        QueryWrapper<Emergency> wrapper = new QueryWrapper<>(params);
        List<Emergency> emergencyList = emergencyMapper.selectList(wrapper);

        return R.data(emergencyList);
    }

    @ApiOperation(value = "保存修改Emergency信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Emergency entity) {

        if (entity.getId() == null) {
            emergencyService.update(
                    Wrappers.<Emergency>lambdaUpdate()
                            .eq(Emergency::getUserId, entity.getUserId())
                            .set(Emergency::getStatus, 0)
            );
            entity.setStatus(1);
            emergencyMapper.insert(entity);
            websocketMessageHandler.broadcastToDoctors(entity);
        } else {
            emergencyMapper.updateById(entity);
        }
        return R.data(entity);
    }

    @ApiOperation(value = "按id删除Emergency，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<String>(Arrays.asList(ids.split(",")));
        emergencyMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "获取正在进行的呼救信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getCurrentCall")
    public R<Emergency> getCurrentCall(String userId) {
        return R.data(emergencyService.getOne(
                Wrappers.<Emergency>lambdaQuery()
                        .eq(Emergency::getUserId, userId)
                        .eq(Emergency::getStatus, 1)
        ));
    }

    @ApiOperation(value = "停止正在进行的呼救信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/stopEmergencyCall")
    public R stopEmergencyCall(String userId) {
        return R.status(emergencyService.update(
                Wrappers.<Emergency>lambdaUpdate()
                        .eq(Emergency::getUserId, userId)
                        .eq(Emergency::getStatus, 1)
                        .set(Emergency::getStatus, 0)
        ));
    }

    @ApiOperation(value = "结束呼救")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "emergencyId", paramType = "String", value = "紧急呼救id", required = true)
    })
    @GetMapping("/stop")
    public R stop(String emergencyId) {
        return R.status(emergencyService.update(
                Wrappers.<Emergency>lambdaUpdate()
                        .eq(Emergency::getId, emergencyId)
                        .set(Emergency::getStatus, 0)
        ));
    }

    @ApiOperation(value = "根据id获取呼救信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationId", paramType = "query", value = "呼救id", required = true)
    })
    @GetMapping("/{emergencyId}")
    public R getById(@PathVariable String emergencyId) {
        return R.data(emergencyService.getUserEmergency(emergencyId));
    }

    @ApiOperation(value = "呼救统计")
    @GetMapping("/getEmergencyStatistics")
    public R getEmergencyStatistics() {
        return R.data(emergencyMapper.selectEmergencyStatistics());
    }

    @ApiOperation(value = "获取用户呼救记录")
    @GetMapping("/getEmergencyList")
    public R getEmergencyList() {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(emergencyService.getEmergencyList(pageDomain));
    }
}
