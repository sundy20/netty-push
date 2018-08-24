package com.sundy.nettypush.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author plus.wang
 * @description 自定义线程池线程命名
 * @date 2018/4/23
 */
public class NamedThreadFactory implements ThreadFactory {

    private boolean daemon = false;

    private final String prefix;

    private final LongAdder threadNumber = new LongAdder();

    public NamedThreadFactory(String prefix) {

        this.prefix = prefix;
    }

    public NamedThreadFactory(String prefix, boolean daemon) {

        this.prefix = prefix;

        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable runnable) {

        threadNumber.add(1);

        Thread myThread = new Thread(runnable, prefix + "@thread-" + threadNumber.intValue());

        myThread.setDaemon(daemon);

        return myThread;
    }
}

