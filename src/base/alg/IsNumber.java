package base.alg;

public class IsNumber {

    public boolean isNumber(String s) {
        // 去除前后的空格
        s = s.trim();
        if (s.length() == 0) {
            return false;
        }
        int i = 0;
        if (s.charAt(i) == '-' || s.charAt(i) == '+') {
            i++;
        }
        int pointNum = 0;
        int digitNum = 0;
        while (i < s.length() && (s.charAt(i) >= '0' && s.charAt(i) <= '9' || s.charAt(i) == '.')) {
            if (s.charAt(i) == '.') {
                pointNum++;
            } else {
                digitNum++;
            }
            i++;
        }
        //只能有一个小数点，整数部分长度至少1个
        if (pointNum > 1 || digitNum < 1) {
            return false;
        }
        if (i == s.length()) {
            return true;
        }
        if (s.charAt(i) == 'e') {
            i++;
            //e 后面必须有数字
            if (i == s.length()) {
                return false;
            }
            if (s.charAt(i) == '-' || s.charAt(i) == '+') {
                i++;
                if (i == s.length()) {
                    return false;
                }
            }
            // 幂后面的数字
            while (i < s.length() && (s.charAt(i) >= '0' && s.charAt(i) <= '9')) {
                i++;
            }

            if (i == s.length()) {
                return true;
            }
        }
        return false;
    }
}
