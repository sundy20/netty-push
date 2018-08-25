package com.sundy.nettypush.component;

import com.sundy.nettypush.util.ThreadPollUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author plus.wang
 * @description
 * @date 2018/8/24
 */
@Component
public class ExecutorComponent {

    private ThreadPoolExecutor threadPoolExecutor;

    @PostConstruct
    public void initThreadPoll() {

        threadPoolExecutor = ThreadPollUtil.defineThreadPool(2, "netty-client");
    }

    @PreDestroy
    public void shutdown() {

        threadPoolExecutor.shutdown();
    }

    public void execute(Runnable runnable) {

        if (!threadPoolExecutor.isShutdown()) {

            threadPoolExecutor.execute(runnable);
        }
    }
}
