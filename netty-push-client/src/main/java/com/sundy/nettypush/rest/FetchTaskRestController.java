package com.sundy.nettypush.rest;

import com.sundy.share.dto.ReqMsg;
import com.sundy.share.dto.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author plus.wang
 * @description 获取任务
 * @date 2018/8/23
 */
@RestController
public class FetchTaskRestController {


    @PostMapping(value = "/nettyclient/fetchtask")
    public Result<String> addTask(@RequestBody ReqMsg reqMsg) {


        return Result.success("");
    }
}
