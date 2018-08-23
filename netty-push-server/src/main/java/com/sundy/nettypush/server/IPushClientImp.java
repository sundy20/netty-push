package com.sundy.nettypush.server;

import com.sundy.share.dto.ReqMsg;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author plus.wang
 * @description
 * @date 2018/8/22
 */
@Component("iPushClientImp")
public class IPushClientImp implements IPushClient {

    private static final Logger logger = LoggerFactory.getLogger(IPushClientImp.class);

    @Override
    public String callClient(ReqMsg reqMsg) {

        SocketChannel channel = (SocketChannel) NettyChannelMap.getChannelByClientId(reqMsg.getClientId());

        if (channel != null) {

            LinkedBlockingQueue<ReqMsg> queue = new LinkedBlockingQueue<>(1);

            NettyChannelMap.putQueue(reqMsg.getReqId(), queue);

            channel.writeAndFlush(reqMsg);
            //同步等待客户端返回消息
            ReqMsg replyMsg;

            try {
                //poll方法非阻塞的 设置超时否则抛出异常null
                replyMsg = queue.poll(10, TimeUnit.SECONDS);

                if (null != replyMsg) {

                    return replyMsg.getJsonStr();
                }

            } catch (InterruptedException e) {

                logger.error("queue poll 消息时超时--------", e);

            } finally {

                NettyChannelMap.removeQueue(reqMsg.getReqId());
            }
        }

        return null;
    }

    public void callClient() {

        final IPushClient iPushClient = new IPushClientImp();

        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 1000, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.DiscardOldestPolicy());

        for (int i = 0; i < 100; i++) {

            executorService.execute(() -> {

                try {

                    ReqMsg reqMsg = new ReqMsg();

                    reqMsg.setClientId("crawler_1");

                    reqMsg.setJsonStr("{'req':'服务器端向客户端：crawler_1 主动请求数据'}");

                    iPushClient.callClient(reqMsg);

                } catch (Exception e) {

                    logger.error("IPushClientImp.callClient error : ", e);
                }
            });
        }
    }
}
