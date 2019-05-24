package base.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorRemoval {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("xiao");
        list.add("he");
        list.add("hans");
        list.add("he");
        Iterator<String> it = list.iterator();

        for (; it.hasNext(); ) {
            String item = it.next();
            if ("he".equals(item)) {
                //use iterator to remove elements
                it.remove();
            }
        }
        //it will encounter ConcurrentModificationException this way
       /* for (String s : list) {
            if ("he".equals(s)) {
                list.remove(s);
            }

        }*/

        System.out.println(list);// return [xiao, hans]
    }
}
