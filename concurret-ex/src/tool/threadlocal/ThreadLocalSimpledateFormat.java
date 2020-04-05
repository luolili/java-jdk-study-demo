package tool.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadLocalSimpledateFormat {

    public String date(int seconds) {
        Date d = new Date(seconds * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String res = format.format(d);
        return res;
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadLocalSimpledateFormat tf = new ThreadLocalSimpledateFormat();
                String date = tf.date(10);
                System.out.println("d: " + date);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadLocalSimpledateFormat tf = new ThreadLocalSimpledateFormat();
                String date = tf.date(1009);
                System.out.println("d: " + date);
            }
        }).start();
    }
}
