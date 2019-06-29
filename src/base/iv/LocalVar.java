package base.iv;

import java.util.HashMap;
import java.util.Map;

/**
 * var types:
 * 1.member var: property+ instance var
 * 2. local: param of method
 * 3. class var:static
 */
public class LocalVar {

    public final String str = "m";//member var

    public static int k;//class var

    public static void say(final int i) {//local var
        System.out.println();
    }

    public static void main(String[] args) {
        Map<Object, String> map = new HashMap<>();
        map.put("d", "hu");
        map.put(1, "hu");
        //System.out.println(map.size());//2
        System.out.println((int) (12 / 10));

    }
}
