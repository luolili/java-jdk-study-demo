package base.alg;

import java.util.Arrays;

/**
 * 出现次数最多的数字
 */
public class MajorityElement {
    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3, 3, 1, 5};
        majorityElement(nums);
    }

    public static int v3(int[] nums) {
        int target = 0;
        int count = 0;
        for (int i = 0; i < nums.length; i++) {

            if (count == 0) {
                target = nums[i];
                count++;
            } else if (target == nums[i]) {
                count++;
            } else {
                count--;
            }
        }
        return target;
    }

    public static int v2(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }

    public static int majorityElement(int[] nums) {
        int target = nums[0];
        int count = 1;
        for (int i = 1; i < nums.length; i++) {
            if (target == nums[i]) {
                count++;
            } else {
                // 为了让 count=0，切换其他元素
                count--;
            }
            //当 count=0 更换其他元素，初始化 count=1
            if (count == 0) {
                target = nums[i];
                count = 1;
            }
        }
        return target;
    }
}
