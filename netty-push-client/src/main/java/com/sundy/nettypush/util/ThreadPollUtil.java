package com.sundy.nettypush.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author plus.wang
 * @description 自定义线程池
 * @date 2018/5/15
 */
public class ThreadPollUtil {

    public static ThreadPoolExecutor defineThreadPool(int coreSize, String namePrefix) {

        return new ThreadPoolExecutor(coreSize, coreSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), new NamedThreadFactory(namePrefix), new ThreadPoolExecutor.AbortPolicy());
    }

    public static ScheduledThreadPoolExecutor defineScheduledThreadPool(int coreSize, String namePrefix) {

        return new ScheduledThreadPoolExecutor(coreSize, new NamedThreadFactory(namePrefix, true));
    }
}
