package com.luo.comparator;

import com.luo.util.Assert;

import java.util.Comparator;

public class NullSafeComparator<T> implements Comparator<T> {


    private final boolean nullsLow;


    private Comparator<T> nonNullComparator;

    private static final NullSafeComparator NULLS_HIGH = new NullSafeComparator<>(false);

    public NullSafeComparator(boolean nullsLow) {
        this.nonNullComparator = ComparableComparator.INSTANCE;
        this.nullsLow = nullsLow;
    }

    public NullSafeComparator(boolean nullsLow, Comparator<T> nonnullComparator) {
        Assert.notNull(nonnullComparator, "non-null comparator is requied");
        this.nullsLow = nullsLow;
        this.nonNullComparator = nonnullComparator;
    }

    /**
     * why to use nullsLow
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(T o1, T o2) {
        if (o1 == o2) {
            return 0;
        }

        //-1 when o1 = null, how to compare them:use nullsLow
        if (o1 == null) {
            return (this.nullsLow ? -1 : 1);
        }

        if (o2 == null) {
            return (this.nullsLow ? 1 : -1);
        }
        //-3 o1 and o2 not null
        return this.nonNullComparator.compare(o1, o2);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof NullSafeComparator)) {
            return false;
        }
        NullSafeComparator otherComp = (NullSafeComparator) other;
        return (this.nonNullComparator.equals(otherComp.nonNullComparator)
                && this.nullsLow == otherComp.nullsLow);

    }

    @Override
    public int hashCode() {
        return this.nonNullComparator.hashCode() * (this.nullsLow ? -1 : 1);
    }

    
    @Override
    public String toString() {
        return "NullSafeComparator: non-null comparator [" + this.nonNullComparator + "]; " +
                (this.nullsLow ? "nulls low" : "nulls high");
    }
}
