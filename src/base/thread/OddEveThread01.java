package base.thread;

/**
 * 试下交替打印odd/even
 * wait/notify
 */
public class OddEveThread01 {

    private static volatile Integer counter = 0;
    private static Object monitor = new Object();


    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (monitor) {
                        if (counter % 2 != 0) {
                            continue;
                        }
                        int i = ++counter;
                        if (i > 100) {
                            return;
                        }
                        System.out.println("奇数：" + i);
                        monitor.notify();
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (monitor) {
                        if (counter % 2 == 0) {
                            continue;
                        }
                        int i = ++counter;
                        if (i > 100) {
                            return;
                        }
                        System.out.println("even：" + i);
                        monitor.notify();
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
