package threadcoreknowledge.createthread;

/**
 * 用Runnable创建Thread
 * 我们传入了Runnable对象，对应源码的target,
 * r若target不为空，就调用target的run方法
 *
 * Runnable更好的原因  ：
 *和Thread类解耦了；
 * 减少创建和销毁Thread的性能损耗；
 * java是单继承，导致他继承了Thread之后，不可以继承其他类
 * 扩展性不强
 */
public class RunnableStyle implements Runnable {
    //具体执行的任务
    @Override
    public void run() {
        System.out.println("use Runnable");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableStyle());

        thread.start();
    }
}
