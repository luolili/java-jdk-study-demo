package tool.threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShutdownNow {
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
        //任务队列 里面没被执行的任务队列
        List<Runnable> runnableList = executorService.shutdownNow();
        System.out.println(runnableList.size());

    }

    static class ShutdownTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(300);
                System.out.println("run--" + Thread.currentThread().getName());
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + " stop");
            }


        }
    }
}
