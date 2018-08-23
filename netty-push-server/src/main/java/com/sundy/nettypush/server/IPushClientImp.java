package com.sundy.nettypush.server;

import com.sundy.nettypush.util.GzipUtil;
import com.sundy.share.dto.Params;
import com.sundy.share.dto.ReqMsg;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author plus.wang
 * @description
 * @date 2018/8/22
 */
public class IPushClientImp implements IPushClient {

    private static final Logger logger = LoggerFactory.getLogger(IPushClientImp.class);

    @Override
    public String callClient(String clientid, String jsonStr) {

        SocketChannel channel = (SocketChannel) NettyChannelMap.get(clientid);

        if (channel != null) {

            ReqMsg askMsg = new ReqMsg();

            Params params = new Params();

            params.setJsonStr(jsonStr);

            String uuid = UUID.randomUUID().toString();

            askMsg.setReqId(uuid);

            askMsg.setParams(params);

            askMsg.setClientId(uuid);

            askMsg.setToclientId(clientid);

            LinkedBlockingQueue<ReqMsg> queue = new LinkedBlockingQueue<>(1);

            NettyChannelMap.putQueue(askMsg.getReqId(), queue);

            channel.writeAndFlush(askMsg);
            //同步等待客户端返回消息
            ReqMsg replyMsg;

            try {
                //poll方法非阻塞的 设置超时否则抛出异常null
                replyMsg = queue.poll(30, TimeUnit.SECONDS);

                if (null != replyMsg) {

                    return GzipUtil.gunzip(replyMsg.getParams().getJsonStr());
                }

            } catch (InterruptedException | IOException e) {

                logger.error("queue poll 消息时超时--------", e);

            } finally {

                NettyChannelMap.removeQueue(askMsg.getReqId());
            }
        }

        return null;
    }

    public void callClient() {

        final IPushClient iPushClient = new IPushClientImp();

        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 1000, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 100; i++) {

            executorService.execute(() -> {

                try {

                    iPushClient.callClient("001", "{'req':'服务器端  向 001 客户端 主动请求数据'}");

                } catch (Exception e) {

                    logger.error("IPushClientImp.callClient error : ", e);
                }
            });
        }
    }
}
