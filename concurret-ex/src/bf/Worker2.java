package bf;

public class Worker2 implements Runnable {
    @Override
    public void run() {
        System.out.println("thread 2 启动");
        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        System.out.println("thread 2 休眠3秒");
    }
}
