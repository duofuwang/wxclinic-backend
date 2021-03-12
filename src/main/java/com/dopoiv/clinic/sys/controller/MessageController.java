package com.dopoiv.clinic.sys.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dopoiv.clinic.common.constants.Result;
import com.dopoiv.clinic.common.tools.BaseController;
import com.dopoiv.clinic.common.utils.UploadUtil;
import com.dopoiv.clinic.sys.QO.Contact;
import com.dopoiv.clinic.sys.entity.Message;
import com.dopoiv.clinic.sys.mapper.MessageMapper;
import com.dopoiv.clinic.websocket.enums.MsgSignFlagEnum;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dov
 * @since 2021-02-28
 */
@RestController
@RequestMapping("/sys/message")
public class MessageController extends BaseController {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserController userController;

    @Value("${web.upload.path}")
    private String uploadPath;

    Logger logger = LoggerFactory.getLogger(getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", paramType = "query", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true)
    })
    @ApiOperation(value = "分页获取Message信息")
    @RequestMapping(method = RequestMethod.POST, value = "/page")
    public Result page(
            Integer pageNum,
            Integer pageSize) {
        Result result = new Result();
        Page<Message> page = new Page<Message>(pageNum, pageSize);
        Message parms = new Message();
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>(parms);

        result.setData(messageMapper.selectPage(page, wrapper));
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Message信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getAllItems")
    public Result getAllItems() {
        Result result = new Result();
        Message parms = new Message();
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>(parms);
        List<Message> messageList = messageMapper.selectList(wrapper);

        result.setData(messageList);
        return result;
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "获取全部Message信息")
    @RequestMapping(method = RequestMethod.POST, value = "/getMsgById")
    public Result getMsgById() {
        Result result = new Result();
        String id = "289b2d758d2b5e41ef33ebf1de920f9a";
        Message message = messageMapper.selectById(id);

        result.setData(message);
        return result;
    }

    @ApiOperation(value = "保存修改Message信息")
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Result save(@RequestBody Message entity) {
        Result result = new Result();
        if (entity.getId() == null) {
            messageMapper.insert(entity);
        } else {
            messageMapper.updateById(entity);
        }
        return result;
    }

    @ApiOperation(value = "按id删除Message，可以传入多个id用，隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "query", value = "传入的id串，用，隔开", required = true)
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public Result delete(String ids) {
        Result result = new Result();
        List<String> deleteIds = new ArrayList<String>();
        for (String id : ids.split(",")) {
            deleteIds.add(id);
        }
        messageMapper.deleteBatchIds(deleteIds);
        return result;
    }

    @ApiOperation(value = "保存用户的聊天信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "message", paramType = "Message", value = "Message对象", required = true)
    })
    public Result saveMsg(Message message) {
        Result result = new Result();
        message
                .setCreateTime(LocalDateTime.now())
                .setSign(MsgSignFlagEnum.UNSIGN.type);

        logger.debug("controller message: {}", message);
        messageMapper.insert(message);
        result.setData(message);
        result.success("信息保存成功");
        return result;
    }

    @ApiOperation(value = "批量签收信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msgIdList", paramType = "List<String>", value = "Message id 集合", required = true)
    })
    public Result batchUpdateMsgSigned(List<String> msgIdList) {
        Result result = new Result();

        for (String mid : msgIdList) {
            UpdateWrapper<Message> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", mid).set("sign", MsgSignFlagEnum.SIGNED.type);
            messageMapper.update(null, updateWrapper);
        }

        result.success("消息批量签收成功：" + msgIdList.size() + "条");

        return result;
    }

    @ApiOperation(value = "根据用户 id 获取最近的 num 条信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户 id", required = true),
            @ApiImplicitParam(name = "num", paramType = "int", value = "数量", required = true),
            @ApiImplicitParam(name = "friendId", paramType = "String", value = "联系人 id", required = true),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getRecentMsg")
    public Result getRecentMsg(String userId, String friendId, int num) {
        Result result = new Result();

        if (!userController.exists(userId)) {
            result.fail("用户不存在");
            return result;
        }

        List<Message> messageList = messageMapper.getRecentMsg(userId, friendId, num);
        result.setData(messageList);
        return result;
    }

    @ApiOperation(value = "获取未读消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getUnreadMsg")
    public Result getUnreadMsg(String userId) {
        Result result = new Result();
        if (!userController.exists(userId)) {
            result.fail("用户不存在");
            return result;
        }
        List<Message> messageList = messageMapper.getUnreadMessage(userId);
        result.setData(messageList);
        return result;
    }

    @ApiOperation(value = "获取最近会话列表和最后一条聊天记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/getContactList")
    public Result getContactList(String userId) {
        Result result = new Result();
        if (!userController.exists(userId)) {
            result.fail("用户不存在");
            return result;
        }
        List<Contact> contactList = messageMapper.getContactList(userId);
        result.setData(contactList);
        return result;
    }

    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "String", value = "用户id", required = true),
            @ApiImplicitParam(name = "file", paramType = "MultipartFile", value = "文件", required = true),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public Result upload(@RequestParam("file") MultipartFile file, String userId) {
        Result result = new Result();
        if (!userController.exists(userId)) {
            result.fail("用户不存在");
            return result;
        }

        logger.debug(file.getOriginalFilename());
        logger.debug(file.getName());
        logger.debug(String.valueOf(file.getSize()));
        if(!UploadUtil.save(file, uploadPath)) {
            result.fail("保存失败");
            return result;
        }
        result.setData("http://localhost:8686/clinic/static/" + file.getOriginalFilename());
        return result;
    }

}
