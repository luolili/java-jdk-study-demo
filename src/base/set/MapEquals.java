package base.set;

import java.util.HashMap;
import java.util.Map;

public class MapEquals {
    public static void main(String[] args) {
        Name name = new Name("hu", "oo");
        Map s1 = new HashMap();
        Map s2 = new HashMap();
        s1.put("1", "11");
        s1.put("2", "11");
        s1.put("3", "11");
        s2.put("1", "11");
        s2.put("3", "11");
        s2.put("2", "11");
        boolean re = s1.equals(s2);
        System.out.println(re);//true
    }
}
