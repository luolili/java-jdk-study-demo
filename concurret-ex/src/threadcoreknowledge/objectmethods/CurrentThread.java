package threadcoreknowledge.objectmethods;

public class CurrentThread implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        new CurrentThread().run();//main
        new Thread(new CurrentThread()).start();
        new Thread(new CurrentThread()).start();
    }
}
