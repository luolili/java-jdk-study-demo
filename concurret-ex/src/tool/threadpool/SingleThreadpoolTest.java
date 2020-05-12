package tool.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程数是1
 * 五个参数
 * 核心线程数：在池子里面的 线程数量，即使他们是空闲的
 * 最大线程数：
 * 等待时间：在线程数 超过 核心线程数的时候，空闲的线程在结束后等待的
 * 时间
 *工作队列：保存提交的任务
 *
 */
public class SingleThreadpoolTest {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Task());

        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("run--" + Thread.currentThread().getName());
            } catch (Exception e) {

            }


        }
    }
}
