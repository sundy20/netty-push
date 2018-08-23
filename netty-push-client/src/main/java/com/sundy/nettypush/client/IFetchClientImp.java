package com.sundy.nettypush.client;

import com.sundy.nettypush.constant.FetchTaskSign;
import com.sundy.share.dto.ReqMsg;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author plus.wang
 * @description
 * @date 2018/8/22
 */
@Component("iFetchClientImp")
public class IFetchClientImp implements IFetchClient {

    private static final Logger logger = LoggerFactory.getLogger(IFetchClientImp.class);

    @Autowired
    private NettyClient nettyClient;

    private ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.DiscardPolicy());

    @PostConstruct
    @Override
    public void fetchTasks() {

        executorService.execute(() -> {

            Channel channel = nettyClient.getChannelFuture().channel();

            while (FetchTaskSign.sign) {

                try {

                    Thread.sleep(8000);

                    logger.info("FetchTaskSign.sign={}", FetchTaskSign.sign);

                    ReqMsg reqMsg = new ReqMsg();

                    reqMsg.setClientId(nettyClient.getClientId());

                    reqMsg.setJsonStr("{'req':'客户端：" + nettyClient.getClientId() + " 主动获取tasks'}");

                    channel.writeAndFlush(reqMsg);

                } catch (InterruptedException e) {

                    logger.error("------------IFetchClientImp.fetchTasks error : ", e);
                }
            }
        });
    }
}
