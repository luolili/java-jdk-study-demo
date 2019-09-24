package threadcoreknowledge.deadlock;

public class MustDeadLock implements Runnable {
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

    public static void main(String[] args) {
        MustDeadLock r1 = new MustDeadLock();
        MustDeadLock r2 = new MustDeadLock();
        Thread thread1 = new Thread(r1);
        Thread thread2 = new Thread(r2);
        thread1.start();
        thread2.start();


    }
}
