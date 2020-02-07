package bf;

public class Worker1 implements Runnable {
    @Override
    public void run() {
        System.out.println("thread 1 启动");
        try {
            Thread.sleep(13000);
        } catch (Exception e) {

        }
        System.out.println("thread 1 休眠13秒");
    }
}
