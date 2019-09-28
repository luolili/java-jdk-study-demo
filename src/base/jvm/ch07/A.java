package base.jvm.ch07;

/**
 * 运行的时候加：-verbose:gc -XX:+PrintGCDetails
 * 指定垃圾收集器：-verbose:gc -XX:+PrintGCDetails -XX:+UseSerialGC
 * Heap
 * def new generation   total 4928K, used 2179K [0x04e00000, 0x05350000, 0x0a350000)
 * eden space 4416K,  49% used [0x04e00000, 0x05020d48, 0x05250000)
 * from space 512K,   0% used [0x05250000, 0x05250000, 0x052d0000)
 * to   space 512K,   0% used [0x052d0000, 0x052d0000, 0x05350000)
 * tenured generation   total 10944K, used 0K [0x0a350000, 0x0ae00000, 0x14e00000)
 * the space 10944K,   0% used [0x0a350000, 0x0a350000, 0x0a350200, 0x0ae00000)
 * Metaspace       used 2255K, capacity 2288K, committed 2368K, reserved 4480K
 */
public class A {
    public static void main(String[] args) {
        byte[] b1 = new byte[4 * 1024];//4mb
    }
}
