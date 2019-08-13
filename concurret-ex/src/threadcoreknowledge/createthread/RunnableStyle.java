package threadcoreknowledge.createthread;

/**
 * 用Runnable创建Thread
 */
public class RunnableStyle implements Runnable {
    @Override
    public void run() {
        System.out.println("use Runnable");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableStyle());

        thread.start();
    }
}
