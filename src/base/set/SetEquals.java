package base.set;

import java.util.HashSet;
import java.util.Set;

public class SetEquals {
    public static void main(String[] args) {
        Name name = new Name("hu", "oo");
        Set s1 = new HashSet();
        Set s2 = new HashSet();
        s1.add(name);
        s2.add(name);
        boolean re = s1.equals(s2);
        System.out.println(re);
    }
}
