package threadcoreknowledge.threadsafe;

/**
 * 运行结果error
 */
public class MultiThreadsError implements Runnable {
    int index = 0;
    static MultiThreadsError instance = new MultiThreadsError();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(instance);
        Thread thread2 = new Thread(instance);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(instance.index);
    }

    @Override
    public void run() {
       /* while (index < 1000) {
            index++;
        }*/
        for (int i = 0; i < 100000; i++) {
            index++;
        }
    }
}
