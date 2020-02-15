package base.alg.string;

/**
 * 把字符串 s 中的每个空格替换成"%20"。
 */
public class ReplaceSpace {

    public String replaceSpace(String s) {
        StringBuilder res = new StringBuilder();
        char[] arr = s.toCharArray();
        for (char c : arr) {
            if (' ' == c) {
                res.append("%20");
            }
            res.append(c);
        }
        return res.toString();
    }
}
