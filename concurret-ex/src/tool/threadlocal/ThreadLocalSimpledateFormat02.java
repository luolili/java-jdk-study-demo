package tool.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalSimpledateFormat02 {

    public String date(int seconds) {
        Date d = new Date(seconds * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String res = format.format(d);
        return res;
    }

    public static void main(String[] args) {


        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 30; i++) {
            final int c = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ThreadLocalSimpledateFormat02 tf = new ThreadLocalSimpledateFormat02();
                    String date = tf.date(10 + c);
                    System.out.println("d: " + date);
                }
            });
        }
    }
}
