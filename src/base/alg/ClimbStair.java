package base.alg;

public class ClimbStair {

    /**
     * 动态规划：把问题分解为子问题，子问题的最优解来构建问题的解
     * 在 第 i-1阶时，爬1阶
     * 在 第 i-2阶时，爬2阶
     * dp[i]=dp[i−1]+dp[i−2]
     *
     * @param n
     * @return
     */
    public int climbStair(int n) {
        if (n == 1) {
            return 1;
        }
        // n+1 数组长度>=3
        int[] dp = new int[n + 1];
        dp[1] = 1;
        //层数是2的时候，有2个方法:1+1,2
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}
