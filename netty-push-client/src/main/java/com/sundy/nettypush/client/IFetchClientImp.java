package com.sundy.nettypush.client;

import com.sundy.nettypush.component.ExecutorComponent;
import com.sundy.nettypush.constant.FetchTaskSign;
import com.sundy.share.dto.ReqMsg;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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

    @Autowired
    private ExecutorComponent executorComponent;

    @PostConstruct
    @Override
    public void fetchTasks() {

        executorComponent.execute(() -> {

            while (FetchTaskSign.sign) {

                try {

                    Channel channel = nettyClient.getChannel();

                    Thread.sleep(6000);

                    if (null != channel) {

                        logger.info("FetchTaskSign.sign={}", FetchTaskSign.sign);

                        ReqMsg reqMsg = new ReqMsg();

                        reqMsg.setClientId(nettyClient.getClientId());

                        reqMsg.setJsonStr("{'req':'客户端：" + nettyClient.getClientId() + " 主动获取tasks'}");

                        channel.writeAndFlush(reqMsg);

                    }
                } catch (InterruptedException e) {

                    logger.error("------------IFetchClientImp.fetchTasks error : ", e);
                }
            }
        });
    }
}
