package com.luo.util;

import com.luo.lang.Nullable;

import java.io.Closeable;
import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public abstract class ClassUtils {

    private static final String ARRAY_SUFFIX = "[]";


    private static final String INTERNAL_ARRAY_PREFIX = "[";

    //for non-primitive array
    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";

    private static final char PACKAGE_SEPARATOR = '.';

    private static final String PATH_SEPARATOR = "/";

    //inner class
    private static final String INNER_CLASS_SEPARATOR = "$";


    public static final String CGLIB_CLASS_SEPARATOR = "$$";
    public static final String CLASS_FILE_SUFFIX = ".class";


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

    //--
    public static void registerCommonClasses(Class<?>... commonClasses) {
        for (Class<?> clazz : commonClasses) {
            commonClassCache.put(clazz.getName(), clazz);

        }
    }

    public static ClassLoader getDefaultClassLoader() {

        ClassLoader cl = null;

        try {

            //-1 get current thread cl first
            cl = Thread.currentThread().getContextClassLoader();//get by current thread
        } catch (Throwable e) {
            // Cannot access thread context ClassLoader - falling back...
        }

        //can not access thread context class loader
        if (cl == null) {

            //-2 use the cl of this class
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {

                try {
                    //-3 use system cl if the cl of this class is null
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable e) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    //--

    /**
     * override current thread context lc when bean cl is not equivalent to it already
     *
     * @param classLoaderToUse
     * @return
     */
    public static ClassLoader overrideThreadContextClassLoader(ClassLoader classLoaderToUse) {
        Thread thread = Thread.currentThread();
        ClassLoader cl = thread.getContextClassLoader();

        if (classLoaderToUse != null && !classLoaderToUse.equals(cl)) {
            thread.setContextClassLoader(classLoaderToUse);
            return classLoaderToUse;
        } else {
            return null;
        }


    }

    /**
     * the name of the potentially primitive class
     *
     * @param name
     * @return
     */
    public static Class<?> resolvePrimitiveClassName(String name) {
        //-1 prepare the returned result and initialize
        Class<?> result = null;
        //-2 get the result from the map, this is how we resolve
        if (name != null && name.length() <= 8) {
            result = primitiveTypeNameMap.get(name);
        }
        return result;
    }

    /**
     * the class loader may be null,which indicates the default cl
     *
     * @param name
     * @param classLoader
     * @return
     * @throws ClassNotFoundException
     * @throws LinkageError
     */
    public static Class<?> forName(String name, @Nullable ClassLoader classLoader)
            throws ClassNotFoundException, LinkageError {
        //-1 first handle primitive class
        Class<?> clazz = resolvePrimitiveClassName(name);
        //-2 if it is not primitive class , get from commonClassCache that includes primitive and wrapper class and other common classes
        if (clazz == null) {
            clazz = commonClassCache.get(name);//from common class cache
        }
        //-3 if not null, just return it
        if (clazz != null) {
            return clazz;
        }

        // -4 if clazz is null,can not get from cache. there are  conditions: string[] style;
        // array condition:  [Ljava.lang.String; [[I

        //like String[] style
        if (name.endsWith(ARRAY_SUFFIX)) {

            String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();

        }

        // "[Ljava.lang.String;" style arrays
        if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {

            String elementClassName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();

        }


        // "[[I" or "[[Ljava.lang.String;" style arrays
        if (name.startsWith(INTERNAL_ARRAY_PREFIX)) {

            String elementClassName = name.substring(INTERNAL_ARRAY_PREFIX.length());
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();

        }
        //-1 prepare the cl
        ClassLoader clToUse = classLoader;
        if (clToUse == null) {
            clToUse = getDefaultClassLoader();
        }


        try {

            return Class.forName(name, false, clToUse);
        } catch (ClassNotFoundException e) {

            int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
            if (lastDotIndex != -1) {
                String innerClassName =
                        name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR + name.substring(lastDotIndex + 1);

                try {
                    return Class.forName(innerClassName, false, clToUse);
                } catch (ClassNotFoundException ex2) {
                    // Swallow - let original exception get through
                }
            }
            throw e;
        }


    }

    //invoke forName method to resolve class name
    public static Class<?> resolveClassName(String className, ClassLoader classLoader) throws ClassNotFoundException {

        try {
            return forName(className, classLoader);
        } catch (IllegalAccessError err) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" +
                    className + "]: " + err.getMessage(), err);
        } catch (LinkageError err) {
            throw new IllegalArgumentException("Unresolvable class definition for class [" + className + "]", err);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Could not find class [" + className + "]", ex);
        }
    }


    public static boolean isPresent(String className, ClassLoader classLoader) {

        try {

            forName(className, classLoader);
            return true;
        } catch (IllegalAccessError err) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" +
                    className + "]: " + err.getMessage(), err);
        } catch (Throwable ex) {
            // notice: Typically ClassNotFoundException or NoClassDefFoundError...
            return false;
        }
    }

    public static boolean isLoadable(Class<?> clazz, ClassLoader classLoader) {

        try {
            return (clazz == classLoader.loadClass(clazz.getName()));
        } catch (ClassNotFoundException ex) {
            // No corresponding class found at all
            return false;
        }
    }

    public static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {

        //-1 default true if cl is null
        if (classLoader == null) {
            return true;
        }
        try {
            //-2 determine by class loader
            if (clazz.getClassLoader() == classLoader) {
                return true;
            }

        } catch (SecurityException ex) {
            // Fall through to loadable check below
        }
        //-3 invoke isLoadable method
        return isLoadable(clazz, classLoader);

    }

    public static boolean isCacheSafe(Class<?> clazz, ClassLoader classLoader) {
        //Assert use
        Assert.notNull(clazz, "Class must not be null");
        try {
            ClassLoader target = clazz.getClassLoader();

            if (target == classLoader || target == null) {
                return true;
            }

            if (classLoader == null) {
                return false;
            }

            //classLoader's parent
            ClassLoader current = classLoader;
            while (current != null) {
                current = current.getParent();
                if (target == current) {
                    return true;
                }

            }

            //target's parent
            while (target != null) {
                target = target.getParent();
                if (target == classLoader) {
                    return false;
                }


            }
        } catch (SecurityException e) {
        }
        // notice: Fallback for ClassLoaders without parent/child relationship:
        // safe if same Class can be loaded from given ClassLoader
        return (classLoader != null && isLoadable(clazz, classLoader));
    }

    public static boolean isPrimitiveWrapper(Class<?> clazz) {

        Assert.notNull(clazz, "Class must not be null");
        return primitiveWrapperTypeMap.containsKey(clazz);

    }

    //determine a class is primitive or wrapper type
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");//pre-check for param clazz
        return clazz.isPrimitive() || isPrimitiveWrapper(clazz);

    }


    public static boolean isPrimitiveArray(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");//pre-check for param clazz
        return clazz.isArray() && clazz.getComponentType().isPrimitive();
    }

    public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");//pre-check for param clazz
        return clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType());
    }

    //lhsType is super, rhsType is sub
    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        Assert.notNull(lhsType, "Left-hand side type must not be null");
        Assert.notNull(rhsType, "Right-hand side type must not be null");
        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        }

        //for lhsType is primitive type
        if (lhsType.isPrimitive()) {
            //-1 get the preimitive of rhsType
            Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);

            if (lhsType == resolvedPrimitive) {
                return true;
            }

        }
        //for lhsType is wrapper type
        else {
            Class<?> resolvedWrapper = primitiveTypeToWraperMap.get(rhsType);
            if (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper)) {
                return true;
            }

        }
        return false;

    }

    public static boolean isAssignableValue(Class<?> type, Object value) {
        return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());
    }

}

