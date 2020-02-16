package base.alg;

public class Power {
    public static void main(String[] args) {
        double res = myPowV3(2, 2);
    }

    /**
     * 实现函数double Power(double base, int exponent)，求base的exponent次方。不得使用库函数，同时不需要考虑大数问题。
     *
     * @param x
     * @param n
     * @return
     */
    public static double myPowV3(double x, int n) {
        long N = n;
        if (n < 0) {
            x = 1 / x;
            N *= -1;
        }
        double res = 1;
        while (N > 0) {
            // N是奇数
            if ((N & 1) == 1) {
                res *= x;
            }
            x *= x;
            N >>>= 1;
        }
        return res;
    }

    public double myPowByRe(double x, int n) {
        long N = n;
        if (N < 0) {
            return 1 / helper(x, -N);
        }
        return helper(x, N);
    }

    public double helper(double x, long n) {
        if (x == 0) {
            return 0;
        }
        if (n == 0) {
            return 1;
        }
        if ((n & 1) == 0) {
            return helper(x, n >>> 1) * helper(x, n >>> 1);
        } else {
            double sq = helper(x, (n - 1) >>> 1) * helper(x, (n - 1) >>> 1);
            return sq * x;
        }
    }

    public double myPowV2(double x, int n) {
        if (x == 0) {
            return 0;
        }
        if (n < 0) {
            x = 1 / x;
            n = -n;
        }
        double res = 1;
        double cur = x;
        for (long i = n; i > 0; i /= 2) {
            if (i % 2 == 1) {
                res = res * cur;
            }
            cur = cur * cur;
        }
        return res;
    }

    /**
     * 暴力法
     *
     * @param x
     * @param n
     * @return
     */
    public double myPow(double x, int n) {
        if (x == 0) {
            return 0;
        }
        if (n < 0) {
            x = 1 / x;
            n = -n;
        }
        double res = 1;
        for (int i = 0; i < n; i++) {
            res *= x;
        }
        return res;
    }
}
