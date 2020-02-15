package base.alg;

import java.util.ArrayList;
import java.util.List;

/**
 * 用两个栈实现队列
 */
public class CQueue {
    //2个数组
    private List<Integer> in = new ArrayList<>();
    ;
    private List<Integer> out = new ArrayList<>();
    ;

    public CQueue() {
        //this.in = new ArrayList<>();
        //this.out = new ArrayList<>();
    }

    public void appendTail(int value) {
        if (value < 1 || value > 10000) {
            return;
        }
        in.add(value);
    }

    public int deleteHead() {
        if (in.size() == 0) {
            return -1;
        }
        if (in.size() == out.size()) {
            return -1;
        }
        Integer outEle = in.get(out.size());
        out.add(in.get(out.size()));
        return outEle;
    }
}
