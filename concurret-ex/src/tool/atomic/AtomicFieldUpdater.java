package tool.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicFieldUpdater implements Runnable {
    private static User a = new User();
    private static User b = new User();

    private AtomicIntegerFieldUpdater scoreUpdater = AtomicIntegerFieldUpdater.newUpdater(User.class, "score");

    static class User {
        volatile int score;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            a.score++;
            scoreUpdater.getAndIncrement(b);

        }
    }

    public static void main(String[] args) throws Exception {
        Thread ta = new Thread(new AtomicFieldUpdater());
        Thread tb = new Thread(new AtomicFieldUpdater());
        ta.start();
        tb.start();
        ta.join();
        tb.join();
        System.out.println("a:" + a.score);
        System.out.println("b:" + b.score);
    }
}
