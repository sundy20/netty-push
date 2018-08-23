package com.sundy.share.dto;

import java.io.Serializable;

/**
 * @author plus.wang
 * @description 传输参数
 * @date 2018/8/22
 */
public class Params implements Serializable {

    private static final long serialVersionUID = 1L;

    private String jsonStr;

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }
}
