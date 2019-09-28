package threadcoreknowledge.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 哟个写代码的方式检测死锁
 */
public class ThreadMXBeanDetection implements Runnable {

    static Object o1 = new Object();
    static Object o2 = new Object();
    int flag = 1;

    @Override
    public void run() {
        System.out.println("flag:" + flag);
        if (flag == 1) {
            synchronized (o1) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (o2) {
                System.out.println("1 get 2");
            }
        }
        if (flag == 0) {
            synchronized (o2) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (o1) {
                System.out.println("2 get 1");
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        ThreadMXBeanDetection r1 = new ThreadMXBeanDetection();
        ThreadMXBeanDetection r2 = new ThreadMXBeanDetection();
        Thread thread1 = new Thread(r1);
        Thread thread2 = new Thread(r2);
        thread1.start();
        thread2.start();

        Thread.sleep(5000);//等他们死锁
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();

        if (deadlockedThreads != null && deadlockedThreads.length > 0) {
            for (long deadlockedThread : deadlockedThreads) {

                ThreadInfo threadInfo = threadMXBean.getThreadInfo(deadlockedThread);
                String threadName = threadInfo.getThreadName();

                System.out.println("name: " + threadName);


            }
        }


    }
}
