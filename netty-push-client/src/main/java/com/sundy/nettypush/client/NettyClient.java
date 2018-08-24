package com.sundy.nettypush.client;

import com.sundy.nettypush.component.ExecutorComponent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author plus.wang
 * @description netty客户端
 * @date 2018/8/23
 */
@Component
public class NettyClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    @Value("${netty.clientId}")
    private String clientId;

    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;
    private int remotePort;
    private String remoteHost;
    private volatile Channel channel;

    @Autowired
    private ExecutorComponent executorComponent;

    public String getClientId() {
        return clientId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void stop() {
        workerGroup.shutdownGracefully();
        logger.info("Stopped Tcp Client: " + getServerInfo());
    }

    private void init(String host, int port) {

        this.remotePort = port;
        this.remoteHost = host;

        workerGroup = new NioEventLoopGroup();
        // 配置客户端NIO线程组
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.group(workerGroup);
        bootstrap.remoteAddress(remoteHost, remotePort);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(new IdleStateHandler(10, 10, 5));
                //添加POJO对象解码器 禁止缓存类加载器
                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                //设置发送消息编码器
                socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(new NettyClientHandler(NettyClient.this));
            }
        });
    }

    @Async
    public void connect(String host, int port) {

        try {

            init(host, port);
            // 发起异步连接操作
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(remoteHost, remotePort)).sync();

            channel = channelFuture.channel();

            channel.closeFuture().sync();

        } catch (Exception e) {

            logger.error("------------NettyClient.doConnect error : ", e);

        } finally {

            try {

                Thread.sleep(3000);

            } catch (InterruptedException e) {

                logger.error("------------NettyClient.doConnect error : ", e);
            }

            executorComponent.execute(() -> connect(host, port));
        }

        /*channelFuture.addListener((ChannelFutureListener) f -> {

            if (f.isSuccess()) {

                logger.info("connect server success---------" + getServerInfo());

            } else {

                logger.error("connect server failed---------" + getServerInfo());

                f.channel().eventLoop().schedule(() -> {

                    try {

                        doConnect();

                    } catch (Exception e) {

                        logger.error("------------NettyClient.doConnect error : ", e);
                    }
                }, 3, TimeUnit.SECONDS);
            }
        });*/
    }

    private String getServerInfo() {
        return String.format("RemoteHost=%s RemotePort=%d", remoteHost, remotePort);
    }
}
