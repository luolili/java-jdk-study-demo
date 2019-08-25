package threadcoreknowledge.threadattr;

/**
 * id 从1开始
 * 通过调试，可以看到还有Finalizer等thread
 */
public class Id {
    public static void main(String[] args) {
        Thread thread = new Thread();

        System.out.println(Thread.currentThread().getId());//1
        System.out.println(thread.getId());// 13
    }
}
