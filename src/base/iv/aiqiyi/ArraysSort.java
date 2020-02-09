package base.iv.aiqiyi;

import java.util.Arrays;

public class ArraysSort {
    public static void main(String[] args) {
        int a[] = {34, 12, 35, 54, 22, 33, 56};
        Arrays.sort(a);
        for (int i = 0; i < a.length; i++) {

            System.out.println(a[i]);

        }

        int[][] mat = {
                {1, 3, 5, 7, 9, 11},
                {0, 2, 4, 6, 8, 10}
        };
        doSomething(mat);

    }

    public static void doSomething(int[][] mat) {
        for (int row = 0; row < mat.length; row++) {
            for (int col = 0; col < mat[0].length; col++) {
                mat[row][col] = mat[row][mat[0].length - 1 - col];
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.print(mat[i][j] + " ");
            }
        }

        System.out.println("---");
        System.out.println(recurse(1));
    }

    /**
     * 递归方法 f(x) = 4 f(x/2)+x 的解
     * 当x/2=1,x=2的 时候，f(2)=4 f(1)+2 ; f(4) = 4 f(2)+4
     * f(0) = 4 f(0),f(0)=0
     *
     * @param n
     * @return
     */
    public static int recurse(int n) {

        if (n == 0) {
            return 0;
        }
        return 4 * recurse(n / 2) + n;
    }
}
