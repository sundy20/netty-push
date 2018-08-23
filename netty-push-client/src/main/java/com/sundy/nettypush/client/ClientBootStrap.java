package com.sundy.nettypush.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author plus.wang
 * @description netty客户端连接
 * @date 2018/8/23
 */
@Component
public class ClientBootStrap {

    @Value("${netty.server.host}")
    private String host;

    @Value("${netty.server.port}")
    private int port;

    @Autowired
    private NettyClient nettyClient;

    @PostConstruct
    public void startNettyServer() {

        nettyClient.connect(host, port);
    }

    @PreDestroy
    public void stopNettyServer() {

        nettyClient.stop();
    }
}
