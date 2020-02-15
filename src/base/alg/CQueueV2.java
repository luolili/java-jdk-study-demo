package base.alg;

import java.util.Stack;

public class CQueueV2 {
    private Stack<Integer> sin = new Stack<>();
    private Stack<Integer> sout = new Stack<>();

    public void appendTail(int value) {
        if (value < 1 || value > 10000) {
            return;
        }
        sin.push(value);
    }

    public int deleteHead() {
        if (sout.isEmpty()) {
            while (!sin.isEmpty()) {
                sout.push(sin.pop());
            }
        }
        return sout.isEmpty() ? -1 : sout.pop();
    }
}
