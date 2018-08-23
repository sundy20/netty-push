package com.sundy.nettypush.server;

public interface IPushClient {

    /**
     * 服务端推送指定客户端
     *
     * @param clientid
     * @param jsonStr
     * @return
     */
    String callClient(String clientid, String jsonStr);
}
