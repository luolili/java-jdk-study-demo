package base.string;

/**
 * how to write equals between two strings
 */
public class EqualsWriting {
    public static void main(String[] args) {
        String s1 = "hello";
        String s2 = "helloo";
        equals01(s1, s2);
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
}
