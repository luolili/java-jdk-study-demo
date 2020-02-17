package base.alg;

/**
 * 给你一根长度为 n 的绳子，请把绳子剪成整数长度的 m 段（m、n都是整数，n>1并且m>1），每段绳子的长度记为 k[0],k[1]...k[m] 。请问 k[0]*k[1]*...*k[m] 可能的最大乘积是多少？例如，当绳子的长度是8时，我们把它剪成长度分别为2、3、3的三段，此时得到的最大乘积是18。
 */
public class CuttingRope {

    public static void main(String[] args) {
        int i = cuttingRope(2);

    }

    /**
     * @param n
     * @return
     */
    public static int cuttingRope(int n) {
        int[] dp = new int[n + 1];
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            // j表示剪短的长度
            for (int j = 1; j <= (i >> 1); j++) {
                // 需要比较 剪短的长度 与 这个长度继续剪的积的结果
                dp[i] = Math.max(dp[i], Math.max(j, dp[j]) * Math.max(i - j, dp[i - j]));
            }
        }
        return dp[n];
    }
}
