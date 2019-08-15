package threadcoreknowledge.startthread;

/**
 * 不能2次调用start
 * java.lang.IllegalThreadStateException
 * 启动线程的时候，会检查启动状态， 加入线程组
 * 调用start0()
 */
public class CantStartTwice {
    public static void main(String[] args) {
        Thread thread = new Thread();
        thread.start();
        thread.start();


    }
}
