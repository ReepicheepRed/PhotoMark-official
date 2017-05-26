package me.jessyan.mvparms.photomark.mvp.model.api.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhiPeng.S on 2017/4/12.
 */

public class ThreadManager {

    //线程池
    private ThreadPoolExecutor mThreadPoolExecutor;

    //线程池配置
    private int corePoolSize = 2;
    private int maximumPoolSize = 10;
    private long keepAliveTime = 60L;
    private TimeUnit unit = TimeUnit.SECONDS;
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

    private ThreadManager() {

    }

    private static volatile ThreadManager instance;

    public static ThreadManager getInstance() {
        if (instance == null) {
            synchronized (ThreadManager.class) {
                if (instance == null) {
                    instance = new ThreadManager();
                }
            }
        }
        return instance;
    }

    public ThreadPoolExecutor Executor(){
        if (mThreadPoolExecutor == null) {
            synchronized (ThreadManager.class) {
                if (mThreadPoolExecutor == null) {
                    mThreadPoolExecutor = new ThreadPoolExecutor(
                            corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
                }
            }
        }
        return mThreadPoolExecutor;
    }

}
