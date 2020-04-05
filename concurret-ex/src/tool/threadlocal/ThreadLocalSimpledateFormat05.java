package tool.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalSimpledateFormat05 {
    public String date(int seconds) {
        Date d = new Date(seconds * 1000);
        //性能更好，并行
        SimpleDateFormat format = ThreadSafeFormatter.tf.get();
        SimpleDateFormat format2 = ThreadSafeFormatter.tf2.get();
        String res = format2.format(d);
        return res;
    }

    public static void main(String[] args) {
        ThreadLocalSimpledateFormat05 t = new ThreadLocalSimpledateFormat05();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 30; i++) {
            final int c = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String date = t.date(10 + c);
                    System.out.println("d: " + date);
                }
            });
        }
    }
}

class ThreadSafeFormatter {
    public static ThreadLocal<SimpleDateFormat> tf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    // lmd 写法
    public static ThreadLocal<SimpleDateFormat> tf2 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
}