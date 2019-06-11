package com.luo.util;

import java.lang.reflect.Array;
import java.util.Arrays;
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
        //---is an array. Array.getLength(obj) is  a native method
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


    public static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2)
            return true;

        if (o1 == null || o2 == null)
            return false;

        if (o1.equals(o2))
            return true;

        if (o1.getClass().isArray() && o2.getClass().isArray())
            return arrayEquals(o1, o2);

        return false;
    }

    public static boolean arrayEquals(Object o1, Object o2) {
        // is a Object[]
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            Arrays.equals((Object[]) o1, (Object[]) o2);
        }
        //--primitive type
        //-1. is a boolean[]
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            Arrays.equals((boolean[]) o1, (boolean[]) o2);
        }

        //-2. is a boolean[]
        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            Arrays.equals((byte[]) o1, (byte[]) o2);
        }

        //-3. is a boolean[]
        if (o1 instanceof char[] && o2 instanceof char[]) {
            Arrays.equals((char[]) o1, (char[]) o2);
        }
        //-4. is a short[]
        if (o1 instanceof short[] && o2 instanceof short[]) {
            Arrays.equals((short[]) o1, (short[]) o2);
        }

        //-5. is a int[]
        if (o1 instanceof int[] && o2 instanceof int[]) {
            Arrays.equals((int[]) o1, (int[]) o2);
        }


        //-6. is a float[]
        if (o1 instanceof float[] && o2 instanceof float[]) {
            Arrays.equals((float[]) o1, (float[]) o2);
        }

        //-7. is a double[]
        if (o1 instanceof double[] && o2 instanceof double[]) {
            Arrays.equals((double[]) o1, (double[]) o2);
        }


        //-8. is a long[]
        if (o1 instanceof long[] && o2 instanceof long[]) {
            Arrays.equals((long[]) o1, (long[]) o2);
        }

        return false;
    }

    //day02--toString
    public static String identityToString(Object obj) {
        if (obj == null) {
            return EMPTY_STRING;
        }

        return obj.getClass().getName() + "@" + getIdentityHexString(obj);

    }

    private static String getIdentityHexString(Object obj) {

        return Integer.toHexString(System.identityHashCode(obj));
    }

    public static String getDisplayString(Object obj) {

        if (obj == null) {
            return EMPTY_STRING;
        }

        return nullSafeToString(obj);
    }

    public static String nullSafeToString(Object obj) {

        if (obj == null) {
            return EMPTY_STRING;
        }

        if (obj instanceof String) {
            return (String) obj;
        }

        if (obj instanceof Object[]) {
            return nullSafeToString((Object[]) obj);
        }

        if (obj instanceof char[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof boolean[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof byte[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof double[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof float[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof long[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof int[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof short[]) {
            return nullSafeToString((char[]) obj);
        }

        String str = obj.toString();

        //return (str==null? EMPTY_STRING:str);
        return (str != null ? str : EMPTY_STRING);
    }

    public static String nullSafeToString(Object[] array) {
        if (array == null) {
            return EMPTY_STRING;
        }

        int len = array.length;

        if (len == 0) {
            return EMPTY_ARRAY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {

            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATE);
            }
            sb.append(String.valueOf(array[i]));

        }
        sb.append(ARRAY_END);
        return sb.toString();
    }


    public static String nullSafeToString(byte[] array) {
        if (array == null) {
            return EMPTY_STRING;
        }

        int len = array.length;

        if (len == 0) {
            return EMPTY_ARRAY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {

            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATE);
            }
            sb.append(String.valueOf(array[i]));

        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(boolean[] array) {
        if (array == null) {
            return EMPTY_STRING;
        }

        int len = array.length;

        if (len == 0) {
            return EMPTY_ARRAY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {

            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATE);
            }
            sb.append(String.valueOf(array[i]));

        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(float[] array) {
        if (array == null) {
            return EMPTY_STRING;
        }

        int len = array.length;

        if (len == 0) {
            return EMPTY_ARRAY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {

            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATE);
            }
            sb.append(String.valueOf(array[i]));

        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(double[] array) {
        if (array == null) {
            return EMPTY_STRING;
        }

        int len = array.length;

        if (len == 0) {
            return EMPTY_ARRAY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {

            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATE);
            }
            sb.append(String.valueOf(array[i]));

        }
        sb.append(ARRAY_END);
        return sb.toString();
    }


    public static String nullSafeToString(long[] array) {
        if (array == null) {
            return EMPTY_STRING;
        }

        int len = array.length;

        if (len == 0) {
            return EMPTY_ARRAY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {

            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATE);
            }
            sb.append(String.valueOf(array[i]));

        }
        sb.append(ARRAY_END);
        return sb.toString();
    }


    public static String nullSafeToString(int[] array) {
        if (array == null) {
            return EMPTY_STRING;
        }

        int len = array.length;

        if (len == 0) {
            return EMPTY_ARRAY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {

            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATE);
            }
            sb.append(String.valueOf(array[i]));

        }
        sb.append(ARRAY_END);
        return sb.toString();
    }


    public static String nullSafeToString(short[] array) {
        if (array == null) {
            return EMPTY_STRING;
        }

        int len = array.length;

        if (len == 0) {
            return EMPTY_ARRAY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {

            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATE);
            }
            sb.append(String.valueOf(array[i]));

        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(char[] array) {
        if (array == null) {
            return EMPTY_STRING;
        }

        int len = array.length;

        if (len == 0) {
            return EMPTY_ARRAY;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {

            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATE);
            }
            sb
                    .append("'")
                    .append(String.valueOf(array[i]))
                    .append("'");

        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

}
