package base.alg;

public class MaxSubsequeuceSum {

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

    public int getMax(int sum, int max) {
        return sum > max ? sum : max;
    }
}
