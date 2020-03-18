package bf;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BigFileReader implements Runnable {
    private static int now;
    private static File file = new File("F:\\githubpro\\java-jdk-study-demo\\concurret-ex\\src\\bf\\bigFile.txt");
    static RandomAccessFile raf = null;
    final int len = 256;

    public BigFileReader() throws IOException {
        raf = new RandomAccessFile(file, "rw");
    }

    @Override
    public void run() {

        while (true) {
            try {
                synchronized (raf) {
                    byte[] b = new byte[len];
                    raf.seek(now);
                    int tmp = raf.read(b);
                    if (tmp == -1) {
                        return;
                    }
                    now += tmp;
                    System.out.println(new String(b));
                    System.out.println("--end-");
                }
            } catch (Exception e) {

            }
        }
    }

    public static void main(String[] args) throws Exception {
        BigFileReader fileReader = new BigFileReader();

        new Thread(fileReader).start();
        new Thread(fileReader).start();
        new Thread(fileReader).start();
    }
}
