package com.sundy.share.dto;

import com.sundy.share.enums.MsgType;

import java.util.UUID;

/**
 * @author plus.wang
 * @description 客户端请求消息实体
 * @date 2018/8/22
 */
public class ReqMsg extends BaseMsg {

    private static final long serialVersionUID = 1L;

    private String reqId = UUID.randomUUID().toString();

    private String jsonStr;

    public ReqMsg() {

        super();

        setType(MsgType.ASK);
    }

    public String getReqId() {
        return reqId;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }
}
