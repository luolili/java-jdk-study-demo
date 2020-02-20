package base.alg;

/**
 * 出现次数最多的数字
 */
public class MajorityElement {
    public int majorityElement(int[] nums) {
        int target = nums[0];
        int count = 1;
        for (int i = 1; i < nums.length; i++) {
            if (target == nums[i]) {
                count++;
            } else {
                // 为了让 count=0，切换其他元素
                count--;
            }
            //当 count=0 更换其他元素，初始化 count=1
            if (count == 0) {
                target = nums[i];
                count = 1;
            }
        }
        return target;
    }
}
