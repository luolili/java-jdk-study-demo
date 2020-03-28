package bf.aqs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 用代码进行压测
 * 一块一块的输出
 */
public class ConcurrencyThreadpoolTest {

    // 线程数
    public static final int threadCount = 10;

    public static void main(String[] args) throws Throwable {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(5);

        //方法2,等3秒执行
        /*exec.schedule(()->{
            System.out.println("ee");
        },3, TimeUnit.SECONDS);*/
        //方法3:间隔执行
        exec.scheduleAtFixedRate(() -> {
            System.out.println("ee");
        }, 1, 3, TimeUnit.SECONDS);
        //scheduleAtFixedRate 不可调用 shutdown
        //exec.shutdown();

    }

}
