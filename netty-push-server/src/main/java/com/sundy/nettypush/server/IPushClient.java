package com.sundy.nettypush.server;

import com.sundy.share.dto.ReqMsg;

/**
 * @author plus.wang
 * @description 主动推送接口
 * @date 2018/8/23
 */
public interface IPushClient {

    /**
     * 调用客户端 推送指定消息
     *
     * @param reqMsg
     * @return
     */
    String callClient(ReqMsg reqMsg);

}
