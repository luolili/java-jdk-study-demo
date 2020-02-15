package base.alg;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 输入整数数组 arr ，找出其中最小的 k 个数。例如，输入4、5、1、6、2、7、3、8这8个数字，则最小的4个数字是1、2、3、4
 */
public class GetLeastNum {

    public int[] getLeastNumV2(int[] arr, int k) {
        int[] res = new int[k];
        Arrays.sort(arr);
        for (int i = 0; i < k; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    public int[] getLeastNum(int[] arr, int k) {
        int[] res = new int[k];
        int p = 0;
        PriorityQueue<Integer> q = new PriorityQueue<>();
        for (int i : arr) {
            q.offer(i);
        }
        while (p != k) {
            res[p++] = q.poll();
        }
        return res;
    }

}
