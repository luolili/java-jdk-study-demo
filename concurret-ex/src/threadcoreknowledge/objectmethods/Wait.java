package threadcoreknowledge.objectmethods;

/**
 * wait / notify用法
 * 代码执行顺序：
 */
public class Wait {

    public static Object object = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread1 thread1 = new Thread1();
        thread1.start();
        Thread.sleep(2000);
        Thread2 thread2 = new Thread2();
        thread2.start();

    }

    static class Thread1 extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + " 开始执行");
                try {
                    object.wait();//释放了锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 获得了锁");

            }
        }
    }

    static class Thread2 extends Thread {
        @Override
        public void run() {
            //这个执行完后释放锁，从而Thread1获得了锁
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + " 调用了nontify");
                object.notify();

            }
        }
    }


}
