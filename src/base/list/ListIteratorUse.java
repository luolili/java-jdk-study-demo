package base.list;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListIteratorUse {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("hs");
        list.add("halo");
        list.add("xi");
        ListIterator<String> it = list.listIterator();
        while (it.hasNext()) {
            if ("hs".equals(it.next())) {
                it.set("doo");//reset the element
                it.add("add");//add an ele after this ele
            }
        }

        System.out.println(list);
    }
}
