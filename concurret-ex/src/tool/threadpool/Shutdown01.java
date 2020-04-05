package tool.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Shutdown01 {
    public static void main(String[] args) throws Exception {
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
        //executorService.execute(new ShutdownTask());
        System.out.println("stat:" + executorService.isShutdown());
//      //在等一会再看 isTerminated
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //false,3s后停没有：没有；5s后停没有：执行完了
        System.out.println("await:" + executorService.awaitTermination(5, TimeUnit.SECONDS));
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
