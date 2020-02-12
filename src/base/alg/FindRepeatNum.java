package base.alg;

import java.util.HashSet;
import java.util.Set;

public class FindRepeatNum {
    /**
     * hash thinking
     *
     * @param nums
     * @return
     */
    public static int findRepeatNumV2(int[] nums) {
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            while (nums[i] != i) {
                if (nums[i] == nums[nums[i]]) {
                    return nums[i];
                }
                int temp = nums[i];
                nums[i] = nums[temp];
                nums[temp] = temp;
            }

        }
        return -1;
    }

    public static int findRepeatNum(int[] nums) {
        int len = nums.length;
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            // 添加失败
            if (!set.add(num)) {
                return num;
            }

        }
        return -1;
    }
}
