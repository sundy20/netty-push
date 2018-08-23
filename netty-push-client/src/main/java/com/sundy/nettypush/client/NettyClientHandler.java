package com.sundy.nettypush.client;

import com.sundy.share.dto.BaseMsg;
import com.sundy.share.dto.PingMsg;
import com.sundy.share.dto.ReqMsg;
import com.sundy.share.enums.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<BaseMsg> {

    private NettyClient nettyClient;

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    public NettyClientHandler(NettyClient client) {

        this.nettyClient = client;
    }

    /**
     * 客户端空闲时执行的方法,心跳的实现,后期失败一定次数后断开通道重连
     *
     * @param ctx
     * @param evt
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    PingMsg pingMsg = new PingMsg();
                    ctx.writeAndFlush(pingMsg);
                    logger.info("NettyClientHandler.userEventTriggered send pingMsg");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("NettyClientHandler.channelActive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("NettyClientHandler.exceptionCaught error : ", cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) {
        MsgType msgType = baseMsg.getType();
        switch (msgType) {

            case PING: {
                //客户端接收到服务端心跳回应
                logger.info("client receive pingMsg");
            }
            break;

            case ASK: {
                ReqMsg reqMsg = (ReqMsg) baseMsg;
                logger.info("客户端clientId: " + nettyClient.getClientId() + " 收到服务端请求消息:" + reqMsg.getJsonStr() + " reqId: " + reqMsg.getReqId());
                reqMsg.setClientId(nettyClient.getClientId());
                reqMsg.setType(MsgType.REPLY);
                channelHandlerContext.writeAndFlush(reqMsg);
            }
            break;

            case REPLY: {

            }
            break;

            default:
                break;
        }

        ReferenceCountUtil.release(baseMsg);
    }
}
