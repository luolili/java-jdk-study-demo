package com.luo.core.convert;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * context for type conversion
 */
public class TypeDescriptor implements Serializable {

    //attrs

    private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
    private static final Map<Class<?>, TypeDescriptor> commonTypesCache = new HashMap<>(32);
    //18个常用类型
    private static final Class<?>[] cached_common_types = {
            boolean.class, char.class, byte.class, int.class,
            short.class, long.class, float.class, double.class,
            Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class,
            String.class, Object.class

    };
    //private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];


}
