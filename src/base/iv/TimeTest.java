package base.iv;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * 实现时间的加减
 */
public class TimeTest {
    public static void main(String[] args) {
        //-1
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        System.out.println(c.getTime());//Fri Aug 02 15:48:39 CST 2019
        //-2
        LocalDateTime now = LocalDateTime.now();

        System.out.println(now);//2019-08-03T15:50:57.665

        LocalDateTime yes = now.minusDays(2);

        System.out.println(yes);
        //格式化
        String re = yes.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println(re);//2019-08-01 15:57:41
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(re, df);
        System.out.println(dateTime);//2019-08-01T16:01:40
        //
       /* try{
            System.out.println();
        }*/
        String res1 = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        String res2 = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("res:" + res1 + "res2:" + res2);


    }
}
