package tool.threadpool;

/**
 * thread pool 优点：
 * 1.响应更快
 * 2.节省内存
 * 3.便于管理
 */
public class ForLoop {

    public static void main(String[] args) {
        // 在 jvm 创建多个线程，gc压力大；有创建线程有上限
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Task());
            thread.start();
        }
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println("run--");
        }
    }
}
