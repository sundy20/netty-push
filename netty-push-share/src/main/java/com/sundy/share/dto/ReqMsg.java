package com.sundy.share.dto;

import com.sundy.share.enums.MsgType;

/**
 * @author plus.wang
 * @description 客户端请求消息实体
 * @date 2018/8/22
 */
public class ReqMsg extends BaseMsg {

	private static final long serialVersionUID = 1L;

	private String reqId;

    private Params params;

	public ReqMsg() {

        super();

        setType(MsgType.ASK);
    }
	
    public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }
}
