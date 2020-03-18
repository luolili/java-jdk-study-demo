package bf;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 对比：4003：5
 */
public class FutureTaskUse {
    private static ExecutorService threads = Executors.newFixedThreadPool(2);
    private static final int t1 = 1000;
    private static final int t2 = 2000;

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
//        getUserInfo();
//        getUserMoney();

        System.out.println("主线程开始：" + Thread.currentThread().getId());
        // 多线程
        Callable<String> userCall = new Callable<String>() {
            @Override
            public String call() throws Exception {
                long us = System.currentTimeMillis();
                System.out.println("子线程1开始：" + Thread.currentThread().getId());
                getUserInfo();
                System.out.println("子线程1结束：" + (System.currentTimeMillis() - us));
                return getUserInfo();
            }
        };
        Callable<Integer> userMoneyCall = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("子线程2：" + Thread.currentThread().getId());
                return getUserMoney();
            }
        };

        FutureTask userTask = new FutureTask(userCall);
        FutureTask userMoneyTask = new FutureTask(userMoneyCall);
//        new Thread(userTask).start();
//        new Thread(userMoneyTask).start();

        threads.submit(userTask);
        threads.submit(userMoneyTask);
        Long end = System.currentTimeMillis();
        System.out.println("主线程结束：" + (end - start));
        System.out.println("用时：" + (end - start));
    }


    public static String getUserInfo() {
        try {
            // 模拟处理业务
            Thread.sleep(t1);
        } catch (Exception e) {

        }
        return "user";
    }

    public static Integer getUserMoney() {
        try {
            Thread.sleep(t2);
        } catch (Exception e) {

        }
        return 20;
    }

}
