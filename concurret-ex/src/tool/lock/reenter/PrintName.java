package tool.lock.reenter;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 电影院预定座位
 */
public class PrintName {
    static class Printer {
        static ReentrantLock lock = new ReentrantLock();

        public void print(String name) {
            int len = name.length();
//            lock.lock();
            try {
                for (int i = 0; i < len; i++) {
                    System.out.print(name.charAt(i) + " ");
                }
                System.out.println("");
            } catch (Exception e) {

            } finally {
                //               lock.unlock();
            }

        }
    }

    public void init() {
        final Printer printer = new Printer();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2);
                } catch (Exception e) {

                }
                printer.print("mee");
            }
        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2);
                } catch (Exception e) {

                }
                printer.print("yum");
            }
        }).start();
    }

    public static void main(String[] args) {
        new PrintName().init();
    }
}
