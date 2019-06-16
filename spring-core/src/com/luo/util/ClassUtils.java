package com.luo.util;

import java.io.Closeable;
import java.io.Externalizable;
import java.io.Serializable;
import java.util.*;

public abstract class ClassUtils {

    private static final String array_suffix = "[]";


    private static final String internal_array_prefix = "[";

    //for non-primitive array
    private static final String non_primitive_array_prefix = "[L";

    private static final char package_separator = '.';

    private static final String path_separator = "/";

    //inner class
    private static final String inner_class_separator = "$";


    public static final String cglib_class_separator = "$$";
    public static final String class_file_suffix = ".class";


    //map with wrapper type -> primitive type: Integer.class ->int.class
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(8);
    //int.class -> Integer.class
    private static final Map<Class<?>, Class<?>> primitiveTypeToWraperMap = new IdentityHashMap<>(8);
    //int -> int.class
    private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<>(32);
    //class name as key, class as value
    private static final Map<String, Class<?>> commonClassCache = new HashMap<>(64);

    private static final Set<Class<?>> javaLanguageInterfaces;

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);

        // notice: Map entry iteration is less expensive to initialize than forEach with lambdas
        for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
            primitiveTypeToWraperMap.put(entry.getValue(), entry.getKey());
            // set wrapper type into common class cache
            registerCommonClasses(entry.getKey());
        }

        Set<Class<?>> primitiveTypes = new HashSet<>(32);
        //-1
        primitiveTypes.addAll(primitiveWrapperTypeMap.values());
        //-2
        Collections.addAll(primitiveTypes, boolean[].class, byte[].class, char[].class, int[].class, short[].class,
                float[].class, double[].class, long[].class);//add primitive array class into primitiveTypes

        //-3
        primitiveTypes.add(void.class);

        //initialize primitiveTypeNameMap
        for (Class<?> primitiveType : primitiveTypes) {
            primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);

        }


        //set wrapper type array
        registerCommonClasses(Boolean[].class, Byte[].class, Character[].class, Short[].class, Integer[].class, Float[].class,
                Double[].class, Long[].class);


        //set other common class
        registerCommonClasses(Number.class, Number[].class, String.class, String[].class, Class.class, Class[].class,
                Object.class, Object[].class);

        //set ex
        registerCommonClasses(Throwable.class, Exception.class, Error.class, RuntimeException.class,
                StackTraceElement.class, StackTraceElement[].class);
        registerCommonClasses(Enum.class, Iterable.class, Iterator.class, Enumeration.class,
                Collection.class, List.class, Set.class, Map.class, Map.Entry.class, Optional.class);
        //common interfaces
        Class<?>[] javaLanguageInterfaceArray = {Serializable.class, Externalizable.class,
                Closeable.class, AutoCloseable.class, Cloneable.class, Comparable.class};
        registerCommonClasses(javaLanguageInterfaceArray);
        javaLanguageInterfaces = new HashSet<>(Arrays.asList(javaLanguageInterfaceArray));
    }

    public static void registerCommonClasses(Class<?>... commonClasses) {
        for (Class<?> clazz : commonClasses) {
            commonClassCache.put(clazz.getName(), clazz);

        }
    }


}
