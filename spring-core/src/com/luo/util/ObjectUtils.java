package com.luo.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public abstract class ObjectUtils {

    //---attrs
    private static final int INITIAL_HASH = 7;
    private static final int MULTIPLIER = 31;

    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = "null";

    private static final String ARRAY_START = "{";
    private static final String ARRAY_END = "}";
    private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;

    private static final String ARRAY_ELEMENT_SEPARATE = ", ";


    /**
     * a non-checked exception means it is a RuntimeException instance
     * or it is an Error instance
     *
     * @param ex
     * @return
     */
    public static boolean isCheckedException(Throwable ex) {
        return !(ex instanceof RuntimeException || ex instanceof Error);
    }

    /**
     * an obj is an array means it is an Object array
     * or a primitive array
     *
     * @param obj
     * @return
     */
    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }


    /**
     * an Object array means it is null
     * or its length is zero
     *
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(Object obj) {

        if (obj == null)
            return true;

        //is an Optional obj.not empty if present
        if (obj instanceof Optional) {
            return !((Optional) obj).isPresent();
        }
        //is a char
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        //is an array
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }

        //is a collection
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        //is a map
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        return false;

    }


}
