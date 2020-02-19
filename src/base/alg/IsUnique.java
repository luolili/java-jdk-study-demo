package base.alg;

/**
 * 确定一个字符串 s 的所有字符是否全都不同。
 */
public class IsUnique {

    public static void main(String[] args) {
        boolean abac = isUnique("abac");
        boolean res = isUniqueV2("abac");


    }

    public static boolean isUniqueV2(String astr) {
        for (int i = 0; i < astr.length() - 1; i++) {
            char ch1 = astr.charAt(i);
            int index = astr.lastIndexOf(ch1);
            if (i != index) {
                return false;
            }
        }
        return true;
    }

    public static boolean isUnique(String astr) {
        for (int i = 0; i < astr.length() - 1; i++) {
            if (astr.indexOf(astr.charAt(i), i + 1) != -1) {
                return false;
            }
        }
        return true;
    }

    public static char firstUniqueChar(String s) {
        char[] chars = new char[26];
        char[] chars1 = s.toCharArray();
        for (char c : chars1) {
            chars[c - 'a']++;
        }
        for (char c : chars1) {
            if (chars[c - 'a'] == 1) {
                return c;
            }
        }
        return ' ';
    }
}
