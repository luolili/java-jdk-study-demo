package base.iv;

import java.util.Random;

/**
 * 随机生成-10  -10
 */
public class RandomUse {

    public static void main(String[] args) {
        Random r = new Random();

        int res = gen();
        for (int i = 0; i < 20; i++) {
            res = gen();
            System.out.println(res);
        }
    }

    public static int gen() {
        Random r = new Random();

        float flag = r.nextFloat();
        return flag > 0.5 ? (int) (r.nextFloat() * 10) : (int) (r.nextFloat() * (-10));
    }
}
