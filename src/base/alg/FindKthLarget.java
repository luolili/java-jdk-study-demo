package base.alg;

import java.util.Arrays;
import java.util.PriorityQueue;

public class FindKthLarget {

    public static void main(String[] args) {
        int[] nums = new int[]{2, 3, 1, 4, 5, 6};
        int res = findKthLargestV2(nums, 2);

    }

    /**
     * 用java的排序
     *
     * @param nums
     * @param k
     * @return
     */
    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    /**
     * heap
     *
     * @param nums
     * @param k
     * @return
     */
    public static int findKthLargestV2(int[] nums, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> a - b);
        for (int num : nums) {
            heap.offer(num);
            if (heap.size() > k) {
                heap.poll();
            }
        }
        return heap.poll();

    }
}
