package base.alg;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ValidBracket {

    public boolean isValid(String s) {
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');
        Stack<Character> stack = new Stack();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            if (map.containsKey(ch)) {
                char topEle = stack.empty() ? '#' : stack.pop();
                if (topEle != map.get(ch)) {
                    return false;
                }

            } else {
                stack.push(ch);
            }
        }

        return stack.isEmpty();
    }
}
