package base.operator;

/**
 * find the single one with ^ operator
 */
public class OnlyOne {
    public static void main(String[] args) {

        int[] nums = {1, 2, 2, 3, 3};
        singleNumber(nums);
        singleChar("hello", "heyllo");
    }

    static int singleNumber(int[] nums) {
        int len = nums.length;

        if (len < 1)
            return 0;

        int res = nums[0];
        for (int i = 1; i < len; i++) {
            res = res ^ nums[i];

        }
        return res;
    }

    static char singleChar(String s1, String s2) {
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        int res = 0;
        for (char c : c1) {
            res ^= c;

        }
        for (char c : c2) {
            res ^= c;
        }

        return (char) res;

    }
}
