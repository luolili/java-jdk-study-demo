package base.array;

/**
 * the largest sum in the arr
 */
public class MaxSubArray {
    public static void main(String[] args) {

        int[] nums = {2, 1, -3, 5, 8, -2};
        maxSubArray(nums);
    }

    static int maxSubArray(int[] nums) {
        int len = nums.length;
        if (len == 1) return nums[0];

        int sum = 0;
        int res = nums[0];
        for (int num : nums) {
            if (sum > 0) {
                sum += num;
            } else {
                sum = num;
            }
            res = Math.max(res, sum);
        }

        return res;
    }
}
