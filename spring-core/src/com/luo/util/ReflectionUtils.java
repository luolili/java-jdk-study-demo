package com.luo.util;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * reflection use and handle its exception
 */
public abstract class ReflectionUtils {


    @FunctionalInterface
    public interface MethodFilter {
        boolean matches(Method method);
    }

    @FunctionalInterface
    public interface FieldFilter {
        boolean matches(Field field);
    }

    /**
     * matches all non-bridge and non-synthetic method
     */
    public static final MethodFilter USER_DECLARED_METODS =
            (method -> (!method.isBridge() && !method.isSynthetic() && method.getDeclaringClass() != Object.class));


    /**
     * match all non-static and non-final
     */
    public static final FieldFilter COPYABLE_FIELD =
            field -> !(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()));


    private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";

    private static final Method[] NO_METHODS = {};
    private static final Field[] NO_FIELD = {};


    /**
     * methods cache
     */
    private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentHashMap<>();

    private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentHashMap<>();


    /**
     * get declared fileds by clazz
     *
     * @param clazz
     * @return
     */
    public static Field[] getDeclaredFields(Class<?> clazz) {

        //-1 get all declared fields from the field cache first
        Field[] result = declaredFieldsCache.get(clazz);

        if (result == null) {

            try {
                result = clazz.getDeclaredFields();//-2 get all declared fields from clazz
                //put the result into the cahce
                declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELD : result));

            } catch (Throwable e) {//-3 catch ex
                throw new IllegalStateException("Failed to introspect class [" + clazz.getName()
                        + "] from the class loader [" + clazz.getClassLoader() + "]", e);
            }


        }

        return result;

    }

    /**
     * find field by its type and name
     *
     * @param clazz the class to introspect
     * @param name  the name of the field
     * @param type  the type of the field
     * @return
     */
    public static Field findField(Class<?> clazz, String name, Class<?> type) {

        //-1 prepare searchType for loop
        Class<?> searchType = clazz;
        //-2 while(searchType) + for (fileds)
        while (Object.class != searchType && searchType != null) {
            Field[] fields = getDeclaredFields(searchType);
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;//-3 if name  and type is null, return the first field. notce: avoid NPE here
                }

            }
            searchType = searchType.getSuperclass();//-4 search field from its parent class

        }
        //-5 else no
        return null;
    }

    /**
     * set the field on the target obj to the value
     *
     * @param field
     * @param target
     * @param value
     */
    public static void setField(Field field, Object target, Object value) {

        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            //handle reflection ex
            handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    //-----handle  reflectio ex

    /**
     * reflection ex includes:
     * 1. NoSuchMethodException
     * 2. IllegalAccessException no access to method
     * 3. InvocationTargetException
     * 4. RuntimeException
     * 5.UndeclaredThrowableException
     *
     * @param e
     */
    public static void handleReflectionException(Exception e) {
        //for method exception
        if (e instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + e.getMessage());
        }

        if (e instanceof IllegalAccessException) {
            throw new IllegalStateException("can not access method: " + e.getMessage());
        }
        //invoke target ex: thrown by an invoked method or constructor
        if (e instanceof InvocationTargetException) {
            handleInvocationTargetException((InvocationTargetException) e);
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }

        throw new UndeclaredThrowableException(e);

    }

    public static void handleInvocationTargetException(InvocationTargetException e) {

        rethrowRuntimeException(e.getTargetException());
    }

    public static void rethrowRuntimeException(Throwable e) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }

        if (e instanceof Error) {
            throw (Error) e;
        }
        throw new UndeclaredThrowableException(e);
    }

    public static void rethrowException(Throwable e) throws Exception {
        if (e instanceof Exception) {
            throw (Exception) e;
        }

        if (e instanceof Error) {
            throw (Error) e;
        }
        throw new UndeclaredThrowableException(e);
    }

    //--

    /**
     * @param field
     * @param target
     * @return the value of the field
     */
    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            handleReflectionException(e);
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + e.getClass().getName() + ": " + e.getMessage());
        }
    }


    //--find method: return null if none found
    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        //-1 prepare the searchType for loop
        Class<?> searchType = clazz;
        while (searchType != null) {
            //-2 if clazz is an interface, get its methods,if not, get its methods and its interface methods
            Method[] methods = searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType);
            for (Method method : methods) {
                //-3 use Arrays.equals to compare two Object[] array
                if (StringUtils.isEmpty(name) || name.equals(method.getName()) && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }

            }
            searchType = searchType.getSuperclass();
        }
        //-4 else no
        return null;
    }

    public static Method findMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name, new Class<?>[0]);
    }
    public static Method[] getDeclaredMethods(Class<?> clazz) {

        Method[] result = declaredMethodsCache.get(clazz);
        if (result == null) {

            try {
                Method[] declaredMethods = clazz.getDeclaredMethods();
                List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
                if (declaredMethods != null) {
                    result = new Method[declaredMethods.length + defaultMethods.size()];
                    System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                    int index = declaredMethods.length;//cursor
                    for (Method defaultMethod : defaultMethods) {
                        result[index] = defaultMethod;
                        index++;

                    }
                } else {//defaultMethods on interfaces are null
                    result = declaredMethods;
                }

                declaredMethodsCache.put(clazz, (result.length == 0 ? NO_METHODS : result));
            } catch (Throwable ex) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
                        "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
            }

        }

        return result;


    }


    //find interface's methods
    public static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;

        for (Class<?> ifc : clazz.getInterfaces()) {
            for (Method ifcMethod : ifc.getMethods()) {
                //interface method is public abstract
                if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(ifcMethod);

                }

            }
        }
        return result;
    }


    //--invoke method
    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception e) {// handle  reflection ex
            handleReflectionException(e);
        }
        throw new IllegalStateException("Should never get here");
    }

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, new Object[0]);
    }

    public static Object invokeJdbcMethod(Method method, Object target, Object... args) throws SQLException {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException e) {
            handleReflectionException(e);
        } catch (InvocationTargetException ex) {

            //handle sql exception
            if (ex.getTargetException() instanceof SQLException) {
                throw (SQLException) ex.getTargetException();
            }

            handleInvocationTargetException(ex);
        }
        throw new IllegalStateException("Should never get here");
    }

    public static Object invokeJdbcMethod(Method method, Object target) throws SQLException {
        return invokeJdbcMethod(method, target, new Object[0]);
    }


    public static boolean declaresException(Method method, Class<?> exceptionType) {
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        for (Class<?> declaredException : exceptionTypes) {
            //exceptionType is declaredException subclass or subinterface
            if (declaredException.isAssignableFrom(exceptionType)) {
                return true;
            }
        }
        return false;


    }

    //--
    public static boolean isPublicStaticFinal(Method method) {
        int modifiers = method.getModifiers();
        return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));

    }

    public static boolean isEqualsMethod(Method method) {
        //if method is not null, so its name is not null
        if (method == null || !method.getName().equals("equals")) {
            return false;
        }

        //determine by its param type==Object.class and param num==1
        Class<?>[] parameterTypes = method.getParameterTypes();
        return (parameterTypes.length == 1 && parameterTypes[0] == Object.class);

    }

    public static boolean isHashCodeMethod(Method method) {
        return (method != null && method.getName().equals("hashCode")
                && method.getParameterCount() == 0);
    }

    public static boolean isObjectMethod(Method method) {
        if (method == null) {
            return false;
        }


        try {
            Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
