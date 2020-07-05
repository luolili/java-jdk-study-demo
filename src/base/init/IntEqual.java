package base.init;

import java.util.Objects;

/**
 * @author : luoli
 * @date : 2020-05-27 15:49
 */
public class IntEqual {
    public static void main(String[] args) {
        long price = 2L;
        Integer a = new Integer(333);
        Integer b = new Integer(333);
        ///TODO
        ///FIXME
        System.out.println(a == b);
        System.out.println(Objects.equals(a, b));
    }
}
