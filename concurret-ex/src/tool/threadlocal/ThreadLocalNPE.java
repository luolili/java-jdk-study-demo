package tool.threadlocal;

/**
 * ThreadLocal 不要放 static 变量
 */
public class ThreadLocalNPE {
    ThreadLocal<Long> tl = new ThreadLocal<>();

    public void set() {
        tl.set(Thread.currentThread().getId());
    }

    public long get() {
        System.out.println(tl.get() instanceof Long);
        System.out.println(tl.get());
        return tl.get();
    }

    /**
     * Long 的null 转化 为long 报NPE
     *
     * @return
     */
    public Long get2() {
        System.out.println(tl.get() instanceof Long);
        System.out.println(tl.get());
        return tl.get();
    }

    public static void main(String[] args) {
        ThreadLocalNPE t = new ThreadLocalNPE();
        //t.get();
        t.get2();
    }
}
