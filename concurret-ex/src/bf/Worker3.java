package bf;

public class Worker3 implements Runnable {
    @Override
    public void run() {
        System.out.println("thread 3 启动");
        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        System.out.println("thread 3 休眠6秒");
    }
}
