package base.iv;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class ComparatorTest {
    public static void main(String[] args) {

        //对整数进行排序
        List<Integer> nums = Arrays.asList(3, 5, 1, 0, -2);

        nums.sort(Comparator.naturalOrder());
        System.out.println(nums);//[-2, 0, 1, 3, 5]
        //对字符串拍下
        List<String> cites = Arrays.asList("Milan", "Lo", "San");

        cites.sort(Comparator.naturalOrder());
        System.out.println(cites);//[Lo, Milan, San]
        

    }
}
