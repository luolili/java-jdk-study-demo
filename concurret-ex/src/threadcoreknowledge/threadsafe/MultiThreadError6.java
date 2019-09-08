package threadcoreknowledge.threadsafe;

import java.util.HashMap;
import java.util.Map;

/**
 * 发布:
 */
public class MultiThreadError6 {


    private Map<String, String> states;

    public MultiThreadError6() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                states = new HashMap<>();
                states.put("1", "11");
                states.put("2", "22");
                states.put("3", "33");
                states.put("4", "44");
                states.put("1", "44");
            }
        }).start();


    }

    public Map<String, String> getStates() {
        return states;
    }

    public static void main(String[] args) throws InterruptedException {
        MultiThreadError6 multiThreadError6 = new MultiThreadError6();
        Map<String, String> states = multiThreadError6.getStates();
        Thread.sleep(15588);
        System.out.println(states.get("1"));//NPE


    }
}