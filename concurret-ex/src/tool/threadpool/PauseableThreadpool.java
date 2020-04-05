package tool.threadpool;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 相同的线程执行不同的方法
 * 防止任务堆积
 * 防止 thread 堆积
 * 排查 thread 数量
 */
public class PauseableThreadpool extends ThreadPoolExecutor {
    private boolean paused;
    final ReentrantLock lock = new ReentrantLock();
    Condition unpaused = lock.newCondition();

    public PauseableThreadpool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    //钩子方法
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        lock.lock();
        try {
            while (paused) {
                //当前线程暂停
                unpaused.await();
            }
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }

    }

    public void resume() {
        lock.lock();
        try {
            paused = false;
            unpaused.signalAll();
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
    }

    public void pause() {
        lock.lock();
        try {
            paused = true;
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        PauseableThreadpool threadpool = new PauseableThreadpool(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("run--");
                try {
                    Thread.sleep(100);
                } catch (Exception e) {

                }
            }
        };

        for (int i = 0; i < 1000; i++) {
            threadpool.execute(r);
        }

        try {
            Thread.sleep(2000);
            threadpool.pause();
            System.out.println("thread pool paused");
            Thread.sleep(2000);
            threadpool.resume();
            System.out.println("thread pool resume");
        } catch (Exception e) {

        }
    }
}
