package bf;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Worker1 worker1 = new Worker1();
        Worker2 worker2 = new Worker2();
        Worker3 worker3 = new Worker3();
        Thread thread1 = new Thread(worker1, "线程1");
        Thread thread2 = new Thread(worker2, "线程2");
        Thread thread3 = new Thread(worker3, "线程3");
        thread1.start();
        thread2.start();
        thread3.start();


        thread1.join();
        thread2.join();
        thread3.join();
        // 主线程 要等 这三个线程运行完
        System.out.println("主线程结束");

    }
}
