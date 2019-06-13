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
    public static boolean isempty(Map<?, ?> map) {
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
}
