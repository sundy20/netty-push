package com.sundy.share.dto;

import com.sundy.share.enums.MsgType;

/**
 * @author plus.wang
 * @description 客户端请求消息实体
 * @date 2018/8/22
 */
public class ReplyMsg extends BaseMsg {

    private static final long serialVersionUID = 1L;

    private String reqId;

    private String jsonStr;

    public ReplyMsg() {

        super();

        setType(MsgType.REPLY);
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }
}
