package threadcoreknowledge.createthread;

/**
 * 用Thread创建Thread
 */
public class ThreadStyle extends Thread {

    @Override
    public void run() {
        System.out.println("use thread");
    }

    public static void main(String[] args) {
        new ThreadStyle().start();
    }
}
