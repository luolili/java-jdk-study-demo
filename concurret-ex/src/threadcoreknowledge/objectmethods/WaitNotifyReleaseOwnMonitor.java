package threadcoreknowledge.objectmethods;

/**
 * wait只释放当前的锁lock
 * Result:
 * threadA got resourceA lock
 * threadA got resourceB lock
 * threadB got resourceA lock
 */
public class WaitNotifyReleaseOwnMonitor {

    private static volatile Object resourceA = new Object();
    private static volatile Object resourceB = new Object();

    public static void main(String[] args) {

        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (resourceA) {
                    System.out.println("threadA got resourceA lock");
                    synchronized (resourceB) {
                        System.out.println("threadA got resourceB lock");
                        try {
                            System.out.println("threadA releases resourceA lock");
                            resourceA.wait();//释放锁
                            //不会打印这句，因为threadB 没有释放锁
                            System.out.println("releases resourceA lock");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (resourceA) {
                    System.out.println("threadB got resourceA lock");
                    synchronized (resourceB) {
                        System.out.println("threadB got resourceB lock");
                    }
                }

            }
        });

        threadA.start();
        threadB.start();
    }
}
