package com.luo.comparator;


import java.util.Comparator;

/**
 * typede factory method
 */

public abstract class Comparators {


    //return a Comparable adapter
    public static <T> Comparator<T> comparable() {

        return ComparableComparator.INSTANCE;
    }
}
