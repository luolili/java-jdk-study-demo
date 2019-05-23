package base.string;

/**
 * 1. how to write equals between two strings
 * 2. starts with a prefix?
 */
public class EqualsWriting {
    public static void main(String[] args) {
        String s1 = "hello";
        String s2 = "hello";
        equals01(s1, s2);
        startsWith01(s1, "he", 1);
    }

    static boolean equals01(String str1, String str2) {

        int len1 = str1.length();
        int len2 = str2.length();
        int i = 0;
        if (len1 == len2) {

            while (len1-- != 0) {
                if (str1.charAt(i) != str2.charAt(i)) {
                    return false;

                }
                i++;
            }
            //return true when the while loop ends
            return true;
        }
        return false;
    }

    static boolean startsWith01(String str, String prefix, int toffset) {

        int len = str.length();
        int to = toffset;
        int preLen = prefix.length();
        if (toffset < 0 || toffset + preLen > len)
            return false;
        int i = 0;
        //-- before preLen because the num of loop is preLen and zero-based
        while (--preLen >= 0) {
            if (str.charAt(to++) != prefix.charAt(i++))
                return false;
        }


        return true;
    }
}
