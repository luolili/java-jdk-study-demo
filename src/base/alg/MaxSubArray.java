package base.alg;

/**
 * dp[i] = max(dp[i-1)+nums[i], nums[i])
 */
public class MaxSubArray {
    public int getMaxSequenceV4(int[] nums) {
        int max = nums[0];
        int sum = 0;
        for (int num : nums) {
            sum = Math.max(sum + num, num);
            max = Math.max(sum, max);
        }
        return max;
    }

    /**
     * dp
     *
     * @param nums
     * @return
     */
    public int getMaxSequenceV3(int[] nums) {
        int max = nums[0];
        int sum = 0;
        for (int num : nums) {
            if (sum > 0) {
                sum += num;
            } else {
                sum = num;
            }
            max = getMax(max, sum);
        }
        return max;
    }

    public int getMaxSubArrayV2(int[] nums) {
        int len = nums.length;
        int max = nums[0];
        for (int i = 1; i < len; i++) {
            if (nums[i - 1] > 0) {
                nums[i] = nums[i - 1];
                max = getMax(nums[i], max);
            }
        }
        return max;
    }

    /**
     * 贪心：
     * 当前元素
     * 当前元素位置的最大和
     * 迄今为止的最大和
     *
     * @param arr
     * @return
     */
    public int getMaxSequece(int[] arr) {
        int sum = arr[0];
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            sum = getMax(sum + arr[i], arr[i]);
            max = getMax(sum, max);
        }
        return max;
    }

    public int getMaxSubArr(int[] nums) {
        int max = nums[0];
        int[] dp = new int[nums.length + 1];
        dp[0] = nums[0];
        int index = 0;
        for (int i = 1; i < nums.length; i++) {
            dp[i] = getMax(dp[i - 1] + nums[i], nums[i]);
            max = getMax(dp[i], max);
            if (dp[i] >= max) {
                index = i;
            }
        }
        System.out.println("index=" + index);
        return max;
    }
    public int getMax(int sum, int max) {
        return sum > max ? sum : max;
    }

    public static void main(String[] args) {

        int[] nums = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};

        MaxSubArray subArray = new MaxSubArray();
        subArray.getMaxSubArr(nums);
    }
}
