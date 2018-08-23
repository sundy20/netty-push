package com.sundy.nettypush.server;

import com.sundy.share.dto.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author plus.wang
 * @description netty服务端处理
 * @date 2018/8/22
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<BaseMsg> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        //客户端非活跃时 客户端死机或者卡死
        super.channelInactive(ctx);

        NettyChannelMap.remove((SocketChannel) ctx.channel());

        ChannelId id = ctx.channel().id();

        logger.info("channel inActive id:" + id);

        String clientid = NettyChannelMap.get(id);

        if (null != clientid) {

            NettyChannelMap.remove(clientid);

            logger.info("-----------客户端" + clientid + "已断开或者无响应-----------");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);

        logger.info("----------成功连接一个客户端----------");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        super.exceptionCaught(ctx, cause);

        logger.error("NettyServerHandler.exceptionCaught : ", cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) {

        switch (baseMsg.getType()) {

            case PING: {
                //服务器端响应客户端心跳 刷新客户端在线时间
                PingMsg pingMsg = (PingMsg) baseMsg;

                PingMsg replyPing = new PingMsg();

                if (null == NettyChannelMap.get(pingMsg.getClientId())) {

                    ChannelId channelId = channelHandlerContext.channel().id();

                    NettyChannelMap.add(pingMsg.getClientId(), (SocketChannel) channelHandlerContext.channel());

                    NettyChannelMap.add(channelId, pingMsg.getClientId());

                    logger.info("clientId : " + pingMsg.getClientId() + " channelId : " + channelId + " receive pingMsg");
                }

                NettyChannelMap.get(pingMsg.getClientId()).writeAndFlush(replyPing);
            }
            break;

            case ASK: {

            }
            break;

            case REPLY: {
                //收到客户端的请求 并刷新客户端在线时间
                ReqMsg replyMsg = (ReqMsg) baseMsg;

                String id = replyMsg.getReqId();

                NettyChannelMap.putMsg(id, replyMsg);
            }
            break;

            default:
                break;
        }

        ReferenceCountUtil.release(baseMsg);
    }
}
