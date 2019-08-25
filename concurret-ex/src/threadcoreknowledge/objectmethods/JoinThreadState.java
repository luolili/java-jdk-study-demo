package threadcoreknowledge.objectmethods;

public class JoinThreadState {

    public static void main(String[] args) throws InterruptedException {
        Thread mainThread = Thread.currentThread();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println(mainThread.getState() + " state");
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
            }
        });
        thread.start();
        System.out.println("主线程开始等，子线程执行完");
        thread.join();
        System.out.println("子线程执行完");


    }
}
