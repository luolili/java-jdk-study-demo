package base.iv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrRemove {

    public static void main(String[] args) {
        int[] nums = new int[]{0, 3, 4, 5, 0, 2, 0, 9};
        int[] res = remove(nums, 0);
        for (int re : res) {
            System.out.println(re);
        }
    }

    public static int[] remove(int[] nums, int target) {
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            list.add(Integer.valueOf(num));
        }
        /**
         * 需要用迭代器的 remove方法来循环删除 list 元素
         */
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            Integer next = it.next();
            if (next == target) {
                it.remove();
            }
        }


        list.remove(target);
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }
}
