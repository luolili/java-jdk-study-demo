package tool.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalSimpledateFormat03 {

    public String date(int seconds) {
        Date d = new Date(seconds * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String res = format.format(d);
        return res;
    }

    public static void main(String[] args) {
        //有重复的时间
        ThreadLocalSimpledateFormat03 tf = new ThreadLocalSimpledateFormat03();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 300; i++) {
            final int c = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String date = tf.date(10 + c);
                    System.out.println("d: " + date);
                }
            });
        }
    }
}
