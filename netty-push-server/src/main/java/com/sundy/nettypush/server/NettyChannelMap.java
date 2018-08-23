package com.sundy.nettypush.server;

import com.sundy.share.dto.ReqMsg;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author plus.wang
 * @description 客户端连接map
 * @date 2018/8/22
 */
public class NettyChannelMap {

    private static final Logger logger = LoggerFactory.getLogger(NettyChannelMap.class);

    /**
     * 保存客户端的channel 唯一标识id  以便服务器向指定客户端推送消息
     */
    private static Map<String, SocketChannel> clientIdSocketChannelMap = new ConcurrentHashMap<>();

    /**
     * 保存推送到客户端没有得到响应的消息 客户端断开连接的情况
     */
    private static Map<String, LinkedBlockingQueue<ReqMsg>> clientNoResMap = new ConcurrentHashMap<String, LinkedBlockingQueue<ReqMsg>>();

    public static void bindClientIdChannel(String clientId, SocketChannel socketChannel) {
        clientIdSocketChannelMap.put(clientId, socketChannel);
    }

    public static Channel getChannelByClientId(String clientId) {
        return clientIdSocketChannelMap.get(clientId);
    }

    public static String getClientId(SocketChannel socketChannel) {
        for (Map.Entry entry : clientIdSocketChannelMap.entrySet()) {
            if (entry.getValue() == socketChannel) {
                return entry.getKey().toString();
            }
        }
        return null;
    }

    public static void remove(SocketChannel socketChannel) {
        for (Map.Entry entry : clientIdSocketChannelMap.entrySet()) {
            if (entry.getValue() == socketChannel) {
                String key = entry.getKey().toString();
                clientIdSocketChannelMap.remove(key);
            }
        }
    }

    public static void remove(String clientId) {
        clientIdSocketChannelMap.remove(clientId);
    }

    public static void putQueue(String reqId, LinkedBlockingQueue<ReqMsg> queue) {
        clientNoResMap.put(reqId, queue);
    }

    public static void putMsg(String reqId, ReqMsg msg) {
        LinkedBlockingQueue<ReqMsg> queue = clientNoResMap.get(reqId);
        if (null == queue) {
            logger.error("NettyChannelMap.putMsg 临时队列不存在!响应消息未放成功!");
            throw new RuntimeException("not found req...");
        }
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            logger.error("NettyChannelMap.putMsg", e);
        }
    }

    public static void removeQueue(String reqId) {
        clientNoResMap.remove(reqId);
    }

    public static Map<String, SocketChannel> getClientIdSocketChannelMap() {
        return clientIdSocketChannelMap;
    }

    public static void setClientIdSocketChannelMap(Map<String, SocketChannel> clientIdSocketChannelMap) {
        NettyChannelMap.clientIdSocketChannelMap = clientIdSocketChannelMap;
    }

}
