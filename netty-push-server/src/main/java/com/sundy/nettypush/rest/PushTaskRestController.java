package com.sundy.nettypush.rest;

import com.sundy.nettypush.server.IPushClient;
import com.sundy.share.dto.ReqMsg;
import com.sundy.share.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author plus.wang
 * @description 推送任务
 * @date 2018/8/23
 */
@RestController
public class PushTaskRestController {

    @Autowired
    @Qualifier("iPushClientImp")
    IPushClient iPushClient;

    @PostMapping(value = "/nettyserver/pushtask")
    public Result<String> addTask(@RequestBody ReqMsg reqMsg) {

        String callClientResultStr = iPushClient.callClient(reqMsg);

        return Result.success(callClientResultStr);
    }
}
