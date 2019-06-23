package com.luo.util;

import com.luo.lang.Nullable;

import java.io.Closeable;
import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;

public abstract class ClassUtils {

    private static final String ARRAY_SUFFIX = "[]";


    private static final String INTERNAL_ARRAY_PREFIX = "[";

    //for non-primitive array
    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";

    private static final char PACKAGE_SEPARATOR = '.';

    private static final char PATH_SEPARATOR = '/';

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

    //resource path to class name
    public static String convertResourcePathToClassName(String resourcePath) {
        Assert.notNull(resourcePath, "Resource path must not be null");
        return resourcePath.replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);
    }

    public static String convertClassNameToResourcePath(String className) {
        Assert.notNull(className, "className path must not be null");
        return className.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }

    public static String classPackageAsResourcePath(Class<?> clazz) {
        if (clazz == null) {
            return "";
        }

        String className = clazz.getName();
        int packageEndIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        if (packageEndIndex == -1) {
            return "";
        }

        //get package name without no class name
        String packageName = className.substring(0, packageEndIndex);

        //package name to path
        return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);


    }

    //param is Collection<?> classes
    public static String classNamesToString(Collection<Class<?>> classes) {
        if (CollectionUtils.isEmpty(classes)) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");

        //use iterator of collection
        for (Iterator<Class<?>> it = classes.iterator(); it.hasNext(); ) {
            Class<?> clazz = it.next();
            sb.append(clazz.getName());
            if (it.hasNext()) {
                sb.append(",");
            }

        }
        sb.append("]");
        return sb.toString();
    }


    //param is Class<?> classes.it's the combined use of Arrays.asList() and the method which param is
    //Collection<?> classes
    public static String classNamesToString(Class<?> classes) {
        return classNamesToString(Arrays.asList(classes));
    }

    /**
     * resource path= package path + resource name
     * resource path uses /
     * package path uses .
     *
     * @param clazz
     * @param resourceName
     * @return the built-up resource path
     */
    public static String addResourcePathToPackagePath(Class<?> clazz, String resourceName) {
        Assert.notNull(resourceName, "Resource name must not be null");

        if (!resourceName.startsWith("/")) {
            //classPackageAsResourcePath: to resource path, add / reparator before adding resource name
            return classPackageAsResourcePath(clazz) + "/" + resourceName;
        }

        return classPackageAsResourcePath(clazz) + resourceName;
    }

    //it's like StringUtils#toStringArray

    /**
     * @param collection
     * @return the class array
     * @see StringUtils#toStringArray(Collection)
     */
    public static Class<?>[] toClassArray(Collection<?> collection) {

        return collection.toArray(new Class<?>[0]);
    }

    /**
     * the lowest method:0.
     * with two params
     *
     * @param clazz
     * @param classLoader
     * @return
     */
    public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz, ClassLoader classLoader) {

        Assert.notNull(clazz, "Class must not be null");
        //-1 the clazz is interface and it is loadable and it has a class loader
        if (clazz.isInterface() && isVisible(clazz, classLoader)) {

            //-2 use set to hold the interfaces
            //Collections.singleton(clazz) creates a singleton set (SingletonSet)
            return Collections.singleton(clazz);
        }

        //-3 if clazz is not interface first . hold with LinkedHashSet
        Set<Class<?>> interfaces = new LinkedHashSet<>();

        Class<?> current = clazz;
        while (current != null) {
            Class<?>[] ifcs = clazz.getInterfaces();
            for (Class<?> ifc : ifcs) {
                //-4 if it is not visible, it can't be loaded by class loader
                if (isVisible(ifc, classLoader)) {
                    interfaces.add(ifc);
                }

            }
            //-5 update the current clazz
            current = current.getSuperclass();

        }

        return interfaces;

    }


    //higher than method with two params. one param
    public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz) {

        return getAllInterfacesForClassAsSet(clazz, null);
    }

    //highest method with Object param
    public static Set<Class<?>> getAllInterfaces(Object instance) {
        Assert.notNull(instance, "Instance must not be null ");
        return getAllInterfacesForClassAsSet(instance.getClass());
    }

    //class's all interfaces to class array
    public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, ClassLoader classLoader) {

        return toClassArray(getAllInterfacesForClassAsSet(clazz, classLoader));
    }

    //class loader may be null
    public static Class<?> createCompositeInterface(Class<?>[] interfaces, ClassLoader classLoader) {

        //-1 the num of interfaces must be less than 65536
        Assert.notNull(interfaces, "Interfaces must not be null");
        return Proxy.getProxyClass(classLoader, interfaces);

    }

    public static Class<?> determineCommonAncestor(Class<?> clazz1, Class<?> clazz2) {
        //-1 if clazz1 is null. the parent is the other
        if (clazz1 == null) {
            return clazz2;
        }

        if (clazz2 == null) {
            return clazz1;
        }

        //-2 clazz1 is parent, clazz2 is child
        if (clazz1.isAssignableFrom(clazz2)) {
            return clazz1;//return parent
        }

        if (clazz2.isAssignableFrom(clazz1)) {
            return clazz1;//return parent
        }
        //-3 only presume the ancestor is clazz1 to start our work
        Class<?> ancestor = clazz1;

        do {
            ancestor = ancestor.getSuperclass();
            if (ancestor == null || Object.class == ancestor) {
                return null;
            }


        } while (!ancestor.isAssignableFrom(clazz2));//when the ancestor is the parent of clazz2
        return ancestor;
    }

    public static boolean isJavaLanguageInterfaces(Class<?> ifc) {

        //it is a set. use set contains method
        return javaLanguageInterfaces.contains(ifc);
    }

    //inner class is non-static
    public static boolean isInnerClass(Class<?> clazz) {
        return clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers());
    }

    //--cglib name - clazz - object
    //check if the specified class name is cglib-generated class
    public static boolean isCglibProxyClassName(@Nullable String className) {
        return (className != null && className.contains(CGLIB_CLASS_SEPARATOR));

    }

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return isCglibProxyClassName(clazz.getName());
    }

    public static boolean isCglibProxy(Object object) {
        return isCglibProxyClass(object.getClass());
    }

    //get user-defined class
    public static Class<?> getUserClass(Class<?> clazz) {
        //cglib-generated class condition
        if (clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && Object.class != superClass) {
                return superClass;
            }

        }
        return clazz;
    }

    public static Class<?> getUserClass(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        return getUserClass(instance.getClass());
    }

    //get the type desc of an obj
    public String getDesriptiveType(Object value) {

        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();

        if (Proxy.isProxyClass(clazz)) {
            StringBuilder sb = new StringBuilder();
            Class<?>[] ifcs = clazz.getInterfaces();
            sb.append(" implementing ");
            for (int i = 0; i < ifcs.length; i++) {
                sb.append(ifcs[i].getName());
                if (i < ifcs.length - 1) {
                    sb.append(",");
                }
            }
            return sb.toString();

        } else {
            return clazz.getTypeName();
        }

    }

    //the given class matches the user-defined type name
    public static boolean matchesTypeName(Class<?> clazz, String typeName) {
        return (typeName != null
                && typeName.equals(clazz.getTypeName()) || typeName.equals(clazz.getSimpleName()));
    }

    public static String getClassFileName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        String className = clazz.getName();

        int lastEndIndex = className.lastIndexOf(PACKAGE_SEPARATOR);

        return className.substring(lastEndIndex + 1) + CLASS_FILE_SUFFIX;

    }


    public static String getPackageName(String fqClassName) {
        Assert.notNull(fqClassName, "Full-qualified class name must not be null");
        int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR);

        return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
    }

    public static String getPackageName(Class<?> clazz) {
        return getPackageName(clazz.getName());
    }

    //it is type name
    public static String getQualifiedName(Class<?> clazz) {
        return clazz.getTypeName();
    }

    //get qualified method name by method and class
    public static String getQualifiedMethodName(Method method, Class<?> clazz) {
        Assert.notNull(method, "Method must not be null");
        //-1 get clazz name
        //-2 add . + method name
        return (clazz != null ? clazz : method.getDeclaringClass()).getName() + "." + method.getName();
    }

    //class == null
    public static String getQualifiedMethodName(Method method) {

        return getQualifiedMethodName(method, null);
    }


    //get constructor by its paramTypes
    public static <T> Constructor<T> getConstructorIfAvailable(Class<T> clazz, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must be not null");
        try {
            return clazz.getConstructor(paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    //check if a class has a constructor
    public static boolean hasConstructor(Class<?> clazz, Class<?>... paramTypes) {
        return (getConstructorIfAvailable(clazz, paramTypes) != null);
    }

    public static Method getMethodIfAvailable(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        //-1 class and method name can't be nul
        Assert.notNull(clazz, "Class must be not null");
        Assert.notNull(methodName, "method name must be not null");
        //-2 if the method has params
        if (paramTypes != null) {
            try {
                //-3 get method by invoking class getMethod(method name, paramTypes
                return clazz.getMethod(methodName, paramTypes);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Expected method not found: " + e);
            }

        }
        //-4 the method no param
        else {
            //-5 use the hashset to hold the unique method
            Set<Method> candidates = new HashSet<>(1);
            //-6 traverse all the methods on class
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    candidates.add(method);
                }

            }
            //-7 there is only one method called methodName
            if (candidates.size() == 1) {
                return candidates.iterator().next();
            }
            //-8 no methodcalled methodName
            else if (candidates.isEmpty()) {
                throw new IllegalStateException("Expected method not found: " + clazz.getName() + '.' + methodName);
            }
            //-9 more than one methods called methodName
            else {
                throw new IllegalStateException("No unique method found: " + clazz.getName() + '.' + methodName);
            }
        }
    }

    public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        return (getMethodIfAvailable(clazz, methodName, paramTypes) != null);
    }

    public static int getMethodCountForName(Class<?> clazz, String methodName) {
        //-1 pre-check
        Assert.notNull(clazz, "Class must be not null");
        Assert.notNull(methodName, "method name must be not null");

        //-2 prepare a count
        int count = 0;
        //-3 get the declared methods and count the num of method called methodName
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (methodName.equals(declaredMethod.getName())) {
                count++;
            }

        }

        //-4 count the methods on interfaces
        Class<?>[] ifcs = clazz.getInterfaces();
        for (Class<?> ifc : ifcs) {
            //-5 invoke the method
            count += getMethodCountForName(ifc, methodName);

        }
        //-5 count the mehtod if the class has super class
        if (clazz.getSuperclass() != null) {
            count += getMethodCountForName(clazz.getSuperclass(), methodName);
        }

        return count;
    }

    //at least one mehtod called methodName
    public static boolean hasAtLeastOneMethodWithName(Class<?> clazz, String methodName) {
        //-1 pre-check
        Assert.notNull(clazz, "Class must be not null");
        Assert.notNull(methodName, "method name must be not null");

        //-2 get all methods
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (methodName.equals(declaredMethod.getName())) {
                return true;
            }
        }
        //-3 get all interfaces
        Class<?>[] ifcs = clazz.getInterfaces();
        for (Class<?> ifc : ifcs) {
            //-4 invoke the method
            if (hasAtLeastOneMethodWithName(ifc, methodName)) {
                return true;
            }

        }

        //-5 if the classhas super class
        return (clazz.getSuperclass() != null && hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName));

    }

    /**
     * find the interface of the class, then find the interface method.
     * if the class has no interface ,just return the original method
     *
     * @param method
     * @return
     */
    public static Method getInterfaceMethodIfPossible(Method method) {
        //-1 when the method's class is public and not an interface
        if (Modifier.isPublic(method.getModifiers()) && !method.getDeclaringClass().isInterface()) {
            //-2 get the class
            Class<?> current = method.getDeclaringClass();
            while (current != null && Object.class != current) {
                //-3 get all interfaces of the class
                Class<?>[] ifcs = current.getInterfaces();
                for (Class<?> ifc : ifcs) {

                    try {
                        //-4 invoke class getMethod(methodName,paramTypes) to get it
                        return ifc.getMethod(method.getName(), method.getParameterTypes());
                    } catch (NoSuchMethodException e) {
                        //nothing: no ex thrown because return the original method at last
                    }

                }

                current = current.getSuperclass();

            }


        }
        return method;
    }

    public static boolean isOverridable(Method method, Class<?> targetClass) {
        //-1 private methods are not overridable
        if (Modifier.isPrivate(method.getModifiers())) {
            return false;
        }
        //-2 public or protected methods are overridable
        if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
            return true;
        }
        //packgae name of the class from method equals with the package name of targetClass
        return (targetClass == null ||
                getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass)));
    }

    @Nullable//returned value can be null
    public static Method getStaticMethod(Class<?> clazz, String methodName, Class<?>... args) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(methodName, "method name must not be null");

        try {
            Method method = clazz.getMethod(methodName, args);
            return Modifier.isStatic(method.getModifiers()) ? method : null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}

