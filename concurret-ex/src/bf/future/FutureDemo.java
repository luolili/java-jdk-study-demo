package bf.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FutureDemo {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 在这里不停下来，继续执行后面的代码：异步计算
        Future<String> f = executorService.submit(FutureDemo::doLongOp);
        System.out.println(System.currentTimeMillis() + " cc");
        // 在调用 get 的时候会阻塞去获取结果，下面的意思是最多等2秒，2秒后没得到结果 返回 null，报错
        String res = f.get(2, TimeUnit.SECONDS);
        System.out.println(System.currentTimeMillis() + " res:" + res);


    }

    public static String doLongOp() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "00";
    }

}
