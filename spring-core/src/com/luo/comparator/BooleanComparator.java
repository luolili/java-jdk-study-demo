package com.luo.comparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * how to write a comparator
 * equals and hashCode method
 */
public class BooleanComparator implements Comparator<Boolean>, Serializable {


    public static final BooleanComparator TRUE_LOW = new BooleanComparator(true);
    public static final BooleanComparator TRUE_HIGH = new BooleanComparator(false);
    private final boolean true_low;

    public BooleanComparator(boolean true_low) {
        this.true_low = true_low;
    }

    /**
     * o1 = false,o2=true -- 1   o1=true,o2=false-- -1
     * o1 = false, o2=false -- 0
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Boolean o1, Boolean o2) {
        return ((o1 ^ o2) ? ((o1 ^ this.true_low) ? 1 : -1) : 0);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() * (true_low ? -1 : 1);
    }

    @Override
    public String toString() {
        return "BooleanComparator" + (true_low ? "true_low" : "true_high");
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj ||
                (obj instanceof BooleanComparator && this.true_low == ((BooleanComparator) obj).true_low));
    }
}
