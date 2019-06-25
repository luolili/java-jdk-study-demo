package com.luo.comparator;


import java.util.Comparator;

/**
 * typede factory method
 */

public abstract class Comparators {


    //return a Comparable adapter
    @SuppressWarnings("unchecked")//add it, no warning
    public static <T> Comparator<T> comparable() {

        return ComparableComparator.INSTANCE;
    }

    //return a Comparable adapter
    public static <T> Comparator<T> nullsLow() {

        NullSafeComparator nullsLow = NullSafeComparator.NULLS_LOW;
        return nullsLow;
    }

    //Return a decorator for the given comparator
    public static <T> Comparator<T> nullsLow(Comparator<T> comparator) {

        return new NullSafeComparator<>(true, comparator);
    }

    //return a Comparable adapter
    public static <T> Comparator<T> nullsHigh() {

        NullSafeComparator nullsLow = NullSafeComparator.NULLS_HIGH;
        return nullsLow;
    }

    //Return a decorator for the given comparator
    public static <T> Comparator<T> nullsHigh(Comparator<T> comparator) {

        return new NullSafeComparator<>(false, comparator);
    }



}
