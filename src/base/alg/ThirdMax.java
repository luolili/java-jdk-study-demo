package base.alg;

import java.util.Arrays;

public class ThirdMax {

    public static void main(String[] args) {
        int[] nums = {1, 2, 2, 5, 3, 5};
        int i = thirdMax(nums);

    }

    public static int thirdMax(int[] nums) {
        int len = nums.length;
        if (len == 1) {
            return nums[0];
        }
        if (len == 2) {
            return nums[0] > nums[1] ? nums[0] : nums[1];
        }
        Arrays.sort(nums);
        int res = 0;
        if (nums[len - 1] != nums[len - 2]) {
            for (int i = len - 3; i >= 0; i--) {

                if (nums[0] == nums[len - 2]) {
                    return nums[len - 1];
                }
                if (nums[i] != nums[len - 2]) {
                    return nums[i];
                }

            }
        }
        if (nums[len - 1] == nums[len - 2]) {
            int n = 1;
            for (int i = len - 3; i >= 0; i--) {
                if (nums[i] != nums[len - 2]) {
                    n = i;
                    break;
                }
            }
            for (int i = n; i >= 0; i--) {
                if (nums[0] == nums[n]) {
                    return nums[len - 1];
                }
                if (nums[i] != nums[n]) {
                    return nums[i];
                }
            }
        }

        return nums[len - 3];
    }
}
