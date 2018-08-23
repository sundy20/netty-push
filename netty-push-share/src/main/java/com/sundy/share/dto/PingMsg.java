package com.sundy.share.dto;

import com.sundy.share.enums.MsgType;

/**
 * @author plus.wang
 * @description 心跳检测的消息实体
 * @date 2018/8/22
 */
public class PingMsg extends BaseMsg {

    private static final long serialVersionUID = 1L;

    public PingMsg() {

        super();

        setType(MsgType.PING);
    }
}
