package com.dopoiv.clinic.project.messageboard.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.web.page.PageDomain;
import com.dopoiv.clinic.project.messageboard.service.IMessageBoardService;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.project.user.service.IUserService;
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
import com.dopoiv.clinic.project.messageboard.mapper.MessageBoardMapper;
import com.dopoiv.clinic.project.messageboard.entity.MessageBoard;

import com.dopoiv.clinic.common.tools.BaseController;

/**
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-05-14
 */
@RestController
@RequestMapping("/message-board")
public class MessageBoardController extends BaseController {
    @Autowired
    private MessageBoardMapper messageBoardMapper;

    @Autowired
    private IMessageBoardService messageBoardService;

    @Autowired
    private IUserService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取MessageBoard信息")
    @GetMapping("/page")
    public R page(MessageBoard params, String startDate, String endDate) {
        PageDomain pageDomain = startMybatisPlusPage();
        return R.data(messageBoardService.getPageForQuery(pageDomain, params, startDate, endDate));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部MessageBoard信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        MessageBoard params = new MessageBoard();
        QueryWrapper<MessageBoard> wrapper = new QueryWrapper<>(params);
        List<MessageBoard> messageBoardList = messageBoardMapper.selectList(wrapper);

        return R.data(messageBoardList);
    }

    @ApiOperation(value = "保存修改MessageBoard信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody MessageBoard entity) {
        if (entity.getId() == null) {
            messageBoardMapper.insert(entity);
        } else {
            messageBoardMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除MessageBoard，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        messageBoardMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "按id获取MessageBoard")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", value = "id", required = true)
    })
    @GetMapping("/{id}")
    public R getById(@PathVariable String id) {
        MessageBoard messageBoard = messageBoardService.getById(id);
        User user = userService.getById(messageBoard.getUserId());
        messageBoard
                .setUserId(user.getId())
                .setNickname(user.getNickname())
                .setRealName(user.getRealName());
        return R.data(messageBoard);
    }
}
