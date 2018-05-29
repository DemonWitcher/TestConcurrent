package com.witcher.lib.executor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {

    //SynchronousQueue 不保留任务的队列  LinkedBlockingQueue保留任务的队列 ArrayBlockingQueue
    public static void main(String[] args) {
        //1个核心线程 0个非核心 LinkedBlockingQueue                  单任务线程池
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        //核心线程0 工作线程无限 存活时间60秒  SynchronousQueue       无限容量线程池 线程保活60秒
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        //全部都是核心线程 LinkedBlockingQueue                       若干个上限的线程池
        ExecutorService executorService3 = Executors.newFixedThreadPool(3);
        //有参数个核心线程 工作线程数量无限 但是存活时间为0  DelayedWorkQueue 一个内部实现的特殊队列
        ExecutorService executorService4 = Executors.newScheduledThreadPool(3);
        //这是什么鬼东西  和threadPoolExecutor不是一个套路
        ExecutorService executorService5 = Executors.newWorkStealingPool();
        //构造函数 SynchronousQueue不保留任务 超过的全进RejectedExecutionHandler  LinkedBlockingQueue
        ExecutorService executorService6 = new ThreadPoolExecutor(0, 3,
                10, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println(r.toString()+"任务被拒绝执行");
            }
        });

        for(int i=0;i<10;++i){
            executorService4.submit(new WorkRunnable("工作任务"+i+"号"));
        }
    }

    private static class WorkRunnable implements Runnable{
        private String name;
        private WorkRunnable(String name){
            this.name = name;
        }
        @Override
        public void run() {
            System.out.println(getTime()+" "+Thread.currentThread().getName()+" "+name+" 开始工作");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(getTime()+" "+Thread.currentThread().getName()+" "+name+" 结束工作");
        }
    }

    private static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

}
