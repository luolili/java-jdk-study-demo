package base.iv;

import java.util.HashSet;
import java.util.Set;

public class ShortSetTest {

    public static void main(String[] args) {
        Set<Short> set = new HashSet<>();
        for (short i = 0; i < 5; i++) {
            set.add(i);
            set.remove(i - 1);//i-1 是 Integer 不能删除Short类型
        }
        System.out.println(set.size());//5

        Set<Short> set2 = new HashSet<>();
        for (short i = 0; i < 5; i++) {
            set2.add(i);
            set2.remove(((short) (i - 1)));
        }
        System.out.println(set2.size());//1
    }
}
