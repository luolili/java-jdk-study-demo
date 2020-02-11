package base.alg;

public class Power {

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
