package com.luo.util;

import java.util.Collection;
import java.util.Map;

/**
 * simepl start of spring-core
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


}
