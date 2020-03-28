package bf.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 用代码进行压测
 * juc5个部分
 * 1.atomic
 * 2.collections:线程安全的集合，queue
 * 3.locks+tool
 * 4.executor:线程调度
 *
 * 安全的策略：
 * 1. 线程独占/私有，如局部变量栈
 * 2.只读的变量，没有修改
 * 3.thread-safe obj
 * 4.被守护对象，通过获取锁来访问
 *
 * 实践：用局部变量；用不可变类；用blockingqueue 实现生产者-消费者
 * 用 Semaphore 创建有界的访问；尽量不用静态变量
 *
 * 提高并发的手段--缓存
 * 缓存可以用到很多地方（读多写少）
 * 缓存的特点：
 * 1.命中率，越高，缓存的作用越大
 * 2.缓存的最大空间，超过这个限制会触发空间清理
 * 2.清理策略：先进先出（对数据的实时性要求高）；lfu:数据使用的次数
 * ；lru（热点数据）；过期时间；随机
 */
public class ConcurrencyTest {

    // 请求次数
    public static final int clientTotal = 1000;
    // 线程和数
    public static final int threadTotal = 50;
    public static int count = 0;

    public static void main(String[] args) throws Throwable {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 控制同一时间的并发数
        final Semaphore semaphore = new Semaphore(threadTotal);
        //阻塞当前 thread
        final CountDownLatch cdl = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    // 判断当前 thread 是否可运行
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (Exception e) {

                }
                cdl.countDown();
            });

        }
        cdl.await();
        executorService.shutdown();
        System.out.println("count=" + count);

    }

    public static void add() {
        count++;
    }
}
