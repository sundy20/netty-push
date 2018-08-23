package com.sundy.nettypush.rest;

import com.sundy.nettypush.constant.FetchTaskSign;
import com.sundy.share.dto.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author plus.wang
 * @description 获取任务
 * @date 2018/8/23
 */
@RestController
public class FetchTaskRestController {

    @GetMapping(value = "/nettyclient/fetchstart")
    public Result<String> startFetchTask() {

        FetchTaskSign.sign = true;

        return Result.success("start fetch tasks success");
    }

    @GetMapping(value = "/nettyclient/fetchstop")
    public Result<String> stopFetchTask() {

        FetchTaskSign.sign = false;

        return Result.success("stop fetch tasks success");
    }
}
