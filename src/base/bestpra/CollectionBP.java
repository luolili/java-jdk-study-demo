package base.bestpra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 集合最佳实践
 */
public class CollectionBP {

    public static void main(String[] args) {
        //1.检查数组是否contain 指定值
        int[] arr = {1, 2, 3};
        int targetVal = 2;
        System.out.println(Arrays.asList(arr).contains(targetVal));

        //2.循环删除集合元素
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            Integer next = it.next();

            it.remove();
        }
        System.out.println(list);
    }
}