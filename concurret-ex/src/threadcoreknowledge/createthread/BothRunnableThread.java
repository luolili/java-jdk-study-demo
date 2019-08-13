package threadcoreknowledge.createthread;

/**
 * 同时用Runnable和Thread方法
 */
public class BothRunnableThread {

    public static void main(String[] args) {
        //打印出i am thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("i am runnable");
            }


        }) {
            @Override
            public void run() {
                System.out.println("i am thread");
            }
        }.start();
    }
}
