package threadcoreknowledge.createthread.wrongways;

public class AnonymousInnerClassDemo {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("--" + Thread.currentThread().getName());

            }
        }).start();

        new Thread(
        ) {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());

            }
        }.start();
    }
}
