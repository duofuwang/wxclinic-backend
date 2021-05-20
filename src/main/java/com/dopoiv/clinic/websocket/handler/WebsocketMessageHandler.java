package com.dopoiv.clinic.websocket.handler;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dopoiv.clinic.common.web.domain.R;
import com.dopoiv.clinic.project.admin.entity.Admin;
import com.dopoiv.clinic.project.admin.mapper.AdminMapper;
import com.dopoiv.clinic.project.emergency.entity.Emergency;
import com.dopoiv.clinic.project.message.controller.MessageController;
import com.dopoiv.clinic.project.message.entity.Message;
import com.dopoiv.clinic.service.DiscardService;
import com.dopoiv.clinic.websocket.DataContent;
import com.dopoiv.clinic.websocket.UserChannelRel;
import com.dopoiv.clinic.websocket.enums.MsgActionEnum;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dov
 */
@Sharable
@Component
public class WebsocketMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    /**
     * 用于记录和管理所有客户端的channel
     */
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DiscardService discardService;

    @Autowired
    MessageController messageController;

    @Autowired
    private AdminMapper adminMapper;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;

            // 获取客户端传来的消息
            String content = textWebSocketFrame.text();
            logger.debug("content: {}", content);

            Channel currentChannel = ctx.channel();

            // 消息包装为对象
            DataContent dataContent = JSONObject.parseObject(content, DataContent.class);
            Integer action = dataContent.getAction();

            logger.debug("消息：{}", dataContent);

            // 判断消息类型，根据不同的类型来处理不同的业务
            if (action.equals(MsgActionEnum.CONNECT.type)) {
                // 当websocket第一次open的时候，初始化channel，把用的channel和userid关联起来

                logger.debug("判断 CONNECT.type");

                String senderId = dataContent.getMessage().getFromId();
                UserChannelRel.put(senderId, currentChannel);

                // 测试
                for (Channel channel : users) {
                    logger.debug("channel id: {}", channel.id().asLongText());
                }
                UserChannelRel.output();
            } else if (action.equals(MsgActionEnum.CHAT.type)) {
                // 聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未读]

                logger.debug("判断 CHAT.type");

                Message message = dataContent.getMessage();
                String msgContent = message.getContent();
                String senderId = message.getFromId();
                String receiverId = message.getToId();

                logger.debug("message: {}", message);
                logger.debug("senderId: {}, receiverId: {}, msgContent: {}", senderId, receiverId, msgContent);

                // 保存消息到数据库，并标记为 <未读>
                R result = messageController.saveMsg(message);
                logger.debug("消息保存结果：{}, {}", result.getCode(), result.getData());

                // 将此消息广播给接收方
                // 从全局用户 Channel 关系中获取接受方的 channel
                Channel receiverChannel = UserChannelRel.get(receiverId);
                if (receiverChannel == null) {
                    // TODO channel为空代表用户离线，推送消息（JPush，个推，小米推送）

                    logger.debug("receiverChannel == null");
                } else {
                    // 当 receiverChannel 不为空的时候，从 ChannelGroup 去查找对应的 channel 是否存在
                    Channel findChannel = users.find(receiverChannel.id());
                    if (findChannel != null) {
                        // 用户在线
                        logger.debug("用户id: {} 在线，发送消息", receiverId);

                        JSONObject data = formatLocalDateTime(dataContent, message.getCreateTime());
                        logger.debug(data.toString());

                        receiverChannel.writeAndFlush(new TextWebSocketFrame(data.toJSONString()));
                    } else {
                        // 用户离线 TODO 推送消息

                        logger.debug("findChannel == null");
                    }
                }

            } else if (action.equals(MsgActionEnum.SIGNED.type)) {
                // 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]

                logger.debug("判断 SIGNED.type");

                // 扩展字段在 SIGNED 类型的消息中，代表需要去签收的消息id，逗号间隔
                String msgIdsStr = dataContent.getExtend();
                String[] msgIds = msgIdsStr.split(",");

                List<String> msgIdList = new ArrayList<>();
                for (String mId : msgIds) {
                    if (StringUtils.isNotBlank(mId)) {
                        msgIdList.add(mId);
                    }
                }

                logger.debug("签收列表: {}", msgIdList);

                if (!msgIdList.isEmpty()) {
                    // 批量签收
                    R result = messageController.batchUpdateMsgSigned(msgIdList);

                    logger.debug("消息签收结果: {}", result);
                }

            }
        } else {
            // 不接受文本以外的数据帧类型
            ctx.channel().writeAndFlush(WebSocketCloseStatus.INVALID_MESSAGE_TYPE).addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的 channel，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        users.add(ctx.channel());
        logger.info("链接创建：{}", ctx.channel().remoteAddress());
    }

    /**
     * 连接断开后移除channel
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        users.remove(ctx.channel());
        logger.info("链接断开：{}", ctx.channel().remoteAddress());
    }

//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) {
//        logger.debug("add..." + ctx.channel().remoteAddress());
//    }

    /**
     * 发生异常之后关闭连接（关闭 channel），并且从 ChannelGroup 中移除
     *
     * @param ctx   上下文
     * @param cause 原因
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().close();
        users.remove(ctx.channel());
    }

    public JSONObject formatLocalDateTime(DataContent dataContent, LocalDateTime localDateTime) {
        String createTime = LocalDateTimeUtil.format(localDateTime, DatePattern.NORM_DATETIME_FORMATTER);
        JSONObject data = JSON.parseObject(JSON.toJSONString(dataContent));
        JSONObject msgObject = data.getJSONObject("message");
        msgObject.put("createTime", createTime);
        data.put("message", msgObject);
        return data;
    }

    public void broadcastToDoctors(Emergency emergency) {
        List<Admin> adminList = adminMapper.selectAll();
        adminList.forEach(doctor -> {
            JSONObject data = new JSONObject();
            data.put("newEmergency", emergency);
            Channel receiverChannel = UserChannelRel.get(doctor.getUserId());
            if (receiverChannel != null) {
                Channel findChannel = users.find(receiverChannel.id());
                if (findChannel != null) {
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(data.toJSONString()));
                }
            }
        });
    }
}