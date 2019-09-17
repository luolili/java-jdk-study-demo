package base.set;

import java.util.HashSet;
import java.util.Set;

public class Name {
    private String first, last;

    public Name(String first, String last) {
        if (first == null || last == null) {
            throw new NullPointerException();
        }
        this.first = first;
        this.last = last;
    }

    //@Override
    public boolean equals(Name o) {
        return first.equals(o.first) && last.equals(o.last);
    }

    //@Override
    public int hashCode() {
        return 31 * first.hashCode() + last.hashCode();
    }

    public static void main(String[] args) {
        Set set = new HashSet();
        set.add(new Name("hu", "njn"));
        boolean re = set.contains(new Name("hu", "njn"));
        System.out.println(re);//alse
    }
}
