package com.sundy.share.dto;

import com.sundy.share.enums.MsgType;

import java.io.Serializable;

/**
 * @author plus.wang
 * @description 抽象消息
 * @date 2018/8/22
 */
public abstract class BaseMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private MsgType type;

    //必须唯一，否者会出现channel调用混乱
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }
}
