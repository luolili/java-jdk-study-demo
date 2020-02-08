package base.alg;

public class TwoSum {


    public static void main(String[] args) {
        int[] nums = {2, 3, 4, 5, 6, 7, 8};
        int[] res = twoSum(nums, 6);

    }

    /**
     * 暴力法
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {

        int len = nums.length;
        for (int i = 0; i < len; i++) {
            //第二个数的值
            int another = target - nums[i];
            for (int j = i + 1; j < len; j++) {
                if (another == nums[j]) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalStateException("");
    }
}
