package threadcoreknowledge.stopthread;

/**
 * 在run方法里面被调用的方法用throws
 * run方法本身用tc
 */
public class RightWayStopThreadInProd implements Runnable {


    @Override
    public void run() {
        while (true) {
            System.out.println("i");
            throwInMethod();
        }
    }

    private void throwInMethod() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new RightWayStopThreadInProd());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
