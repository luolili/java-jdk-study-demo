package base.thread;

public class OddEvenThread02 {

    private static volatile boolean loopForOdd = true;
    private static volatile boolean loopForEven = true;
    private static volatile int counter = 1;

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    while (loopForOdd) {

                    }
                    int counter = OddEvenThread02.counter;
                    if (counter > 100) {
                        break;
                    }
                    System.out.println("odd:" + counter);
                    OddEvenThread02.counter++;
                    loopForOdd = true;
                    loopForEven = false;
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    while (loopForEven) {

                    }
                    int counter = OddEvenThread02.counter;
                    if (counter > 100) {
                        break;
                    }
                    System.out.println("even:" + counter);
                    OddEvenThread02.counter++;
                    loopForEven = true;
                    loopForOdd = false;
                }
            }
        }).start();
        loopForOdd = false;
    }


}
