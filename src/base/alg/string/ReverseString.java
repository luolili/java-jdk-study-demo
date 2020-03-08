package base.alg.string;

public class ReverseString {

    public static void main(String[] args) {
        String s = "halo";
        char[] str = s.toCharArray();
        reverseString(str);
        for (char c : str) {
            System.out.println(c);
        }
    }

    public static void reverseString(char[] s) {
        //异或：不同为1
        int left = 0, right = s.length - 1;
        while (left < right) {
            s[left] ^= s[right];
            s[right] ^= s[left];
            s[left++] ^= s[right--];
        }
    }

    public static void reverseStringV2(char[] s) {
        char tmp;
        for (int left = 0, right = s.length - 1; left < right; left++, right--) {
            tmp = s[left];
            s[left] = s[right];
            s[right] = tmp;
        }
    }
}
