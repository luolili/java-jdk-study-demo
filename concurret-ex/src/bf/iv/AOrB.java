package bf.iv;

/**
 * a,b2个线程同时进行，a的run方法里面b.sleep()
 * 此时a阻塞，还是b
 */
public class AOrB {
    public static void main(String[] args) {

        Thread b = new Thread(() -> {
            try {

            } catch (Exception e) {

            }
        });
        Thread a = new Thread(() -> {
            try {

            } catch (Exception e) {

            }
        });
    }
}
