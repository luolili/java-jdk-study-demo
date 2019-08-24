package threadcoreknowledge.objectmethods;

public class Join {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "--end");
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "--end");
            }
        });

        thread1.start();
        thread2.start();
        System.out.println("--开始等待子线程执行完");
        thread1.join();
        thread2.join();//主线程等待2个子线程执行完
        System.out.println("--all end");
    }
}
