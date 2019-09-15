package threadcoreknowledge.threadsafe;

import java.util.HashMap;
import java.util.Map;

/**
 * 发布:
 */
public class MultiThreadError3 {
    private Map<String, String> states;

    public MultiThreadError3() {
        this.states = new HashMap<>();
        states.put("1", "11");
        states.put("2", "22");
        states.put("3", "33");
        states.put("4", "44");
        states.put("1", "44");
    }

    public Map<String, String> getStates() {
        return states;
    }

    //解决方法：副本代替states
    public Map<String, String> getStatesImprove() {
        return new HashMap<>(states);
    }

    public static void main(String[] args) {
        MultiThreadError3 multiThreadError3 = new MultiThreadError3();
        Map<String, String> states = multiThreadError3.getStates();
       /* System.out.println(states.get("1"));
        states.remove("1");
        System.out.println(states.get("1"));//null*/
        Map<String, String> states2 = multiThreadError3.getStatesImprove();
        System.out.println(states2.get("1"));
        states2.remove("1");
        System.out.println(states2.get("1"));
        System.out.println(states.get("1"));


    }
}