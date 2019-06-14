package com.luo.util;

import java.util.*;

/**
 * simple start of spring-core
 */
public abstract class CollectionUtils {


    /**
     * that a collection is empty means it is null
     * or it is empty
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * it is the same as collection
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }


    /**
     * source is the potentially primitive array
     *
     * @param source
     * @return
     */
    public static List arrayToList(Object source) {
        return Arrays.asList(ObjectUtils.toObjectArray(source));
    }

    /**
     * put the source array into a collection
     *the type of element of collection is E
     * @param source
     * @param c
     * @param <E>
     */
    public static <E> void mergeArrayIntoCollection(Object source, Collection<E> c) {
        Object[] array = ObjectUtils.toObjectArray(source);

        for (Object item : array) {
            c.add((E) item);

        }
    }

    /**
     * check if the given iterator has the element
     *the type of the element of iterator is ?
     * @param it
     * @param element
     * @return
     */
    public static boolean contains(Iterator<?> it, Object element) {
        if (it != null) {
            while (it.hasNext()) {
                Object next = it.next();
                //return ObjectUtils.nullSafeEquals(next, element);
                if (ObjectUtils.nullSafeEquals(next, element)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containsInstance(Collection<?> collection, Object ele) {
        if (collection != null) {
            for (Object candidate : collection) {

                if (candidate == ele) {
                    return true;
                }

            }
        }
        return false;
    }

    public static boolean containsAny(Collection<?> collection, Collection<?> candidates) {
        if (isEmpty(collection) || isEmpty(candidates)) {
            return false;
        }

        for (Object candidate : candidates) {
            if (collection.contains(candidate)) {
                return true;
            }
        }
        return false;
    }


    /**
     * return the first ele in candidates that is contained in source
     *
     * @param source
     * @param candidates
     * @param <E>
     * @return
     */
    public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return null;
        }
        //the type of candidate is Object,not E
        for (Object candidate : candidates) {

            if (source.contains(candidate)) {
                return (E) candidate;
            }

        }
        return null;
    }


    /**
     * find a single (element) value of the given type in collection
     *
     * @param collection
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {

        if (isEmpty(collection)) {
            return null;
        }
        T value = null;
        for (Object candidate : collection) {
            if (type == null || type.isInstance(candidate)) {
                if (value != null) {
                    return null;//has more than one element type in collection
                }
                value = (T) candidate;
            }

        }
        return value;
    }
}
