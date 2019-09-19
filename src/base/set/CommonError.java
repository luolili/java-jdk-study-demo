package base.set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 10个常见错误
 */
public class CommonError {
    public static void main(String[] args) {
        //error
        int[] arr = {1, 2, 3};
        List<int[]> arrList = Arrays.asList(arr);
        //right
        List arrList2 = new ArrayList(Arrays.asList(arr));
        //循环删除list元素

        List<String> arrList3 = new ArrayList(Arrays.asList("a", "ds", "n", "k"));
        for (int i = 0; i < arrList3.size(); i++) {
            arrList3.remove(i);
        }

        System.out.println(arrList3);
        //or
        for (String s : arrList3) {
            if (s.equals("a")) {
                arrList3.remove(s);
            }
        }

        System.out.println("foreach:" + arrList3);
    }
}
