package com.sundy.nettypush.server;

import com.sundy.share.dto.BaseMsg;
import com.sundy.share.dto.PingMsg;
import com.sundy.share.dto.ReqMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author plus.wang
 * @description netty服务端处理器
 * @date 2018/8/22
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<BaseMsg> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        //客户端非活跃时 客户端死机或者卡死
        super.channelInactive(ctx);

        SocketChannel socketChannel = (SocketChannel) ctx.channel();

        String clientid = NettyChannelMap.getClientId(socketChannel);

        if (null != clientid) {

            logger.info("NettyServerHandler.channelInactive 客户端 clientId : " + clientid + " 已断开或者无响应");

            NettyChannelMap.remove(clientid);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);

        logger.info("NettyServerHandler.channelActive 成功连接一个客户端");
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

                if (null == NettyChannelMap.getChannelByClientId(pingMsg.getClientId())) {

                    NettyChannelMap.bindClientIdChannel(pingMsg.getClientId(), (SocketChannel) channelHandlerContext.channel());
                }

                logger.info("NettyServerHandler.channelRead0 receive pingMsg from clientId : " + pingMsg.getClientId());

                PingMsg replyPing = new PingMsg();

                NettyChannelMap.getChannelByClientId(pingMsg.getClientId()).writeAndFlush(replyPing);
            }
            break;

            case ASK: {

            }
            break;

            case REPLY: {
                //收到客户端的请求
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
