package threadcoreknowledge.unncaughtexception;

/**
 * 1. no try catch:
 * 2.
 */
public class CantCatchDirectly implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        try {
            //try是针对主线程的，无法捕获到子线程的ex,可以在run里面tc
            new Thread(new CantCatchDirectly(), "t1").start();
            Thread.sleep(100);
            new Thread(new CantCatchDirectly(), "t2").start();
            Thread.sleep(100);
            new Thread(new CantCatchDirectly(), "t3").start();
            Thread.sleep(100);
            new Thread(new CantCatchDirectly(), "t4").start();
        } catch (RuntimeException e) {
            System.out.println("caught ex");//未打印
        }

    }

    @Override
    public void run() {
        try {
            throw new RuntimeException();
        } catch (Exception e) {
            System.out.println("caught ex");
        }
    }
}
