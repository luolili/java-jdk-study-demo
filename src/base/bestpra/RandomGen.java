package base.bestpra;

import java.util.concurrent.ThreadLocalRandom;

public class RandomGen {

    public static void main(String[] args) {
        ThreadLocalRandom.current().nextInt(10);
    }
}
