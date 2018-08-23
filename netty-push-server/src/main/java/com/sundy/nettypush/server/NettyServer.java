package com.sundy.nettypush.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author plus.wang
 * @description netty服务
 * @date 2018/8/23
 */
@Component
public class NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private EventLoopGroup boss;

    private EventLoopGroup worker;

    public void stop() {
        try {
            boss.shutdownGracefully().await();
            worker.shutdownGracefully().await();
            boss = null;
            worker = null;
        } catch (InterruptedException e) {
            logger.error("---------Stopped Tcp Server error : ", e);
        }
    }

    @Async
    public void start(int port) {
        logger.info("---------Start Netty Tcp Server Port : " + port + "---------");
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        // 配置服务器的NIO线程租
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                p.addLast(new ObjectEncoder());
                p.addLast(new NettyServerHandler());
            }
        });

        try {
            // 绑定端口，线程同步阻塞等待服务器绑定到指定端口
            ChannelFuture f = bootstrap.bind(port).sync();
            //成功绑定到端口之后,给channel增加一个管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("---------Start Netty Tcp Server Port : " + port + " error : ", e);
        }
    }
}
