package base.alg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LenOfLongestSubstring {
    public int lengthOfLongestSubstringV3(String s) {
        int res = 0;
        int n = s.length();
        Map<Character, Integer> map = new HashMap<>();
        int i = 0, j = 0;
        for (j = 0; j < n; j++) {
            if (map.containsKey(s.charAt(j))) {
                i = Math.max(map.get(s.charAt(j)), i);
            }
            res = Math.max(res, j - i + 1);
            map.put(s.charAt(j), j + 1);
        }
        return res;
    }
    /**
     * 滑动窗口
     *
     * @param s
     * @return
     */
    public int lengthOfLongestSubstringV2(String s) {
        int res = 0;
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int i = 0, j = 0;
        while (i < n && j < n) {
            if (!set.contains(s.charAt(j))) {
                set.add(s.charAt(j++));
                res = Math.max(res, j - i);
            } else {
                set.remove(i++);
            }
        }
        return res;
    }

    public int lengthOfLongestSubstring(String s) {
        int res = 0;
        int n = s.length();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (allUnique(s, i, j)) {
                    res = Math.max(res, j - i);
                }
            }

        }
        return res;
    }

    /**
     * 判断一个字符串 是否有相同的字符
     *
     * @param substrinng
     * @param start
     * @param end
     * @return
     */
    public boolean allUnique(String substrinng, int start, int end) {
        Set<Character> set = new HashSet<>();
        int len = substrinng.length();
        for (int i = 0; i < len; i++) {
            Character c = substrinng.charAt(i);
            if (set.contains(c)) {
                return false;
            }
            set.add(c);
        }
        return true;
    }
}
