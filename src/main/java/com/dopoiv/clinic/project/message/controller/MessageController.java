package com.dopoiv.clinic.project.message.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.utils.SecurityUtil;
import com.dopoiv.clinic.common.utils.UploadUtil;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.message.dto.ContactDTO;
import com.dopoiv.clinic.project.message.dto.MessageStatistics;
import com.dopoiv.clinic.project.message.entity.Message;
import com.dopoiv.clinic.project.message.mapper.MessageMapper;
import com.dopoiv.clinic.project.message.service.impl.MessageServiceImpl;
import com.dopoiv.clinic.project.user.entity.User;
import com.dopoiv.clinic.websocket.enums.MsgSignFlagEnum;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 控制器类
 * </p>
 *
 * @author wangduofu
 * @since 2021-04-24
 */
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageServiceImpl messageService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Message信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public R page(
            Integer pageNum,
            Integer pageSize) {
        Page<Message> page = new Page<>(pageNum, pageSize);
        Message params = new Message();
        QueryWrapper<Message> wrapper = new QueryWrapper<>(params);

        return R.data(messageMapper.selectPage(page, wrapper));
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Message信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public R getAllItems() {
        Message params = new Message();
        QueryWrapper<Message> wrapper = new QueryWrapper<>(params);
        List<Message> messageList = messageMapper.selectList(wrapper);

        return R.data(messageList);
    }

    @ApiOperation(value = "保存修改Message信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public R save(@RequestBody Message entity) {
        if (entity.getId() == null) {
            messageMapper.insert(entity);
        } else {
            messageMapper.updateById(entity);
        }
        return R.success();
    }

    @ApiOperation(value = "按id删除Message，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public R delete(String ids) {
        List<String> deleteIds = new ArrayList<>(Arrays.asList(ids.split(",")));
        messageMapper.deleteBatchIds(deleteIds);
        return R.success();
    }

    @ApiOperation(value = "保存用户的聊天信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "message", paramType = "Message", value = "Message对象", required = true)
    })
    public R saveMsg(Message message) {
        message
                .setCreateTime(LocalDateTime.now())
                .setSign(MsgSignFlagEnum.UNSIGN.type);

        logger.debug("controller message: {}", message);
        messageMapper.insert(message);
        return R.data(message);
    }

    @ApiOperation(value = "批量签收信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msgIdList", paramType = "List<String>", value = "Message id 集合", required = true)
    })
    public R batchUpdateMsgSigned(List<String> msgIdList) {
        boolean success = messageService.update(
                Wrappers.<Message>lambdaUpdate()
                        .in(Message::getId, msgIdList)
                        .set(Message::getSign, MsgSignFlagEnum.SIGNED.getType())
        );
        if (success) {
            return R.success("消息批量签收成功：" + msgIdList.size() + "条");
        }
        return R.error("签收失败");
    }

    @ApiOperation(value = "获取用户最近的 num 条信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", paramType = "int", value = "数量"),
            @ApiImplicitParam(name = "friendId", paramType = "String", value = "联系人 id", required = true),
    })
    @GetMapping("/getRecentMsg")
    public R getRecentMsg(String friendId, int num) {
        User user = SecurityUtil.getUserInfo();
        List<Message> messageList = messageService.getRecentMsg(user.getId(), friendId, num);
        return R.data(messageList);
    }

    @ApiOperation(value = "获取未读消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getUnreadMsg")
    public R getUnreadMsg(String userId) {
        List<Message> messageList = messageMapper.getUnreadMessage(userId);
        return R.data(messageList);
    }

    @ApiOperation(value = "获取最近会话列表和最后一条聊天记录")
    @GetMapping("/getContactList")
    public R getContactList() {
        User user = SecurityUtil.getUserInfo();
        List<ContactDTO> contactList = messageMapper.getContactList(user.getId());
        return R.data(contactList);
    }

    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "file", paramType = "MultipartFile", value = "文件", required = true),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public R<String> upload(@RequestParam("file") MultipartFile file, String userId) {

        logger.debug(file.getOriginalFilename());
        logger.debug(file.getName());
        logger.debug(String.valueOf(file.getSize()));
        String filename = UploadUtil.save(file);
        if(StrUtil.isEmpty(filename)) {
            return R.error("保存失败");
        }
        return R.data("http://localhost:8686/clinic/static/" + filename);
    }

    @ApiOperation(value = "获取消息统计数据")
    @GetMapping("/getMessageStatistics")
    public R<List<MessageStatistics>> getMessageStatistics() {
        return R.data(messageMapper.selectMessageStatistics());
    }
}
