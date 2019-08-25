package threadcoreknowledge.objectmethods;

public class JoinPrincipal {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    System.out.println("thread 1 finished");
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " finished");
            }
        });
        thread.start();
        System.out.println("主线程开始等，子线程执行完");
        //thread.join();
        synchronized (thread) {
            thread.wait();
        }
        System.out.println("子线程执行完");
    }
}
