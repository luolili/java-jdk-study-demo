package base.alg;

/**
 * 输入一个整数，输出该数二进制表示中 1 的个数。例如，把 9 表示成二进制是 1001，有 2 位是 1。因此，如果输入 9，则该函数输出 2。
 */
public class HammingWeight {

    public int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            count += (n & 1);
            n >>>= 1;
        }
        return count;
    }

    public int hammingWeightV2(int n) {
        // 可以看到，n & (n - 1)n&(n−1) 得到的结果，就是将 n 最低位 1，换成 0 之后的值。
        int count = 0;
        while (n != 0) {
            n &= n - 1;
            count++;
        }
        return count;
    }

    public int hammingWeightV3(int n) {

        return Integer.bitCount(n);
    }
}
