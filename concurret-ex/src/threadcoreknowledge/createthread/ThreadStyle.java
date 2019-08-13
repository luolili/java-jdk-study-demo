package threadcoreknowledge.createthread;

/**
 * 用Thread创建Thread
 * ThreadStyle 重写了run方法
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
