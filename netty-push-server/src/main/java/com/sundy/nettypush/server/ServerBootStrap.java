package com.sundy.nettypush.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author plus.wang
 * @description netty启动
 * @date 2018/8/23
 */
@Component
public class ServerBootStrap {

    @Value("${netty.server.port:9090}")
    private int nettyPort;

    @Autowired
    private NettyServer nettyServer;

    @PostConstruct
    public void startNettyServer() {

        nettyServer.start(nettyPort);
    }

    @PreDestroy
    public void stopNettyServer() {

        nettyServer.stop();
    }
}
