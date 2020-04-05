package tool.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Shutdown {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 100; i++) {
            executorService.execute(new ShutdownTask());
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //已有的任务执行完才关闭
        System.out.println("stat:" + executorService.isShutdown());
        executorService.shutdown();
        //拒绝新任务
        //executorService.execute(new ShutdownTask());
        System.out.println("stat:" + executorService.isShutdown());
        //在等一会再看 isTerminated
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //false
        System.out.println("termi:" + executorService.isTerminated());
    }

    static class ShutdownTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(300);
                System.out.println("run--" + Thread.currentThread().getName());
            } catch (Exception e) {

            }


        }
    }
}
