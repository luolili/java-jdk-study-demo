package bf;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 主线程 依赖 县城A初始化数据，才可继续加载后续逻辑
 */
public class CountDownArticle {
    public static void main(String[] args) throws InterruptedException {
        AtomicReference<String> key = new AtomicReference<>(" ");
        // 当countDown 方法 的技术是0 的时候，才会执行await方法后面的代码
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Thread t = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                key.set("密钥12345");
                System.out.println("数据1初始完");
                // 释放
                countDownLatch.countDown();
                System.out.println("数据2初始完");
                countDownLatch.countDown();
                System.out.println("数据3初始完");
                countDownLatch.countDown();
            } catch (Exception e) {

            }
        });
        t.start();
        // 等待 数据初始完
        countDownLatch.await();
        System.out.println("key:" + key.get());


    }
}
