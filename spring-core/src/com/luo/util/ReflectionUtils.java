package com.luo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
    public static final MethodFilter user_declared_metods =
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

        //get all declared fields from the field cache first
        Field[] result = declaredFieldsCache.get(clazz);

        if (result == null) {

            try {
                result = clazz.getDeclaredFields();//get all declared fields from clazz
                //put the result into the cahce
                declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELD : result));

            } catch (Throwable e) {//catch ex
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

        //prepare searchType for loop
        Class<?> searchType = clazz;
        //while(searchType) + for (fileds)
        while (Object.class != searchType && searchType != null) {
            Field[] fields = getDeclaredFields(searchType);
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;//if name  and type is null, return the first field. notce: avoid NPE here
                }

            }
            searchType = searchType.getSuperclass();//search field from its parent class

        }

        return null;
    }

}
