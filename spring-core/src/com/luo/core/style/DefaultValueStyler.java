package com.luo.core.style;

import com.luo.lang.Nullable;
import com.luo.util.ClassUtils;
import com.luo.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 将对象转换为字符串的风格
 */
public class DefaultValueStyler implements ValueStyler {
    //attr ,tag
    private static final String EMPTY = "[empty]";
    private static final String NULL = "[null]";
    private static final String COLLECTION = "collection";
    private static final String SET = "set";
    private static final String LIST = "list";
    private static final String MAP = "map";
    private static final String ARRAY = "array";

    @Override
    public String style(@Nullable Object value) {
        if (value == null) {
            return NULL;
        } else if (value instanceof String) {
            return "\'" + value + "\'";
        } else if (value instanceof Class) {
            return ClassUtils.getShortName((Class<?>) value);
        } else if (value instanceof Method) {
            //-1 类型转换
            Method method = (Method) value;
            return method.getName() + "@" + ClassUtils.getShortName(method.getDeclaringClass());
        } else if (value instanceof Map) {
            return style((Map) value);
        } else if (value instanceof Map.Entry) {
            return style((Map.Entry) value);
        } else if (value instanceof Collection) {
            return style((Collection) value);
        } else if (value.getClass().isArray()) {
            return styleArray(ObjectUtils.toObjectArray(value));
        } else {
            return String.valueOf(value);//基础类型的数组或基础类型
        }
    }


    private <K, V> String style(Map<K, V> map) {

        //-1 创建结果
        StringBuilder sb = new StringBuilder(map.size() * 8 + 16);

        sb.append(MAP + "[");

        for (Iterator<Map.Entry<K, V>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<K, V> entry = it.next();
            sb.append(style(entry));//调用的是style(Map.Entry value)
            if (it.hasNext()) {
                sb.append(',').append(' ');//单引号
            }

        }
        if (map.isEmpty()) {
            sb.append(EMPTY);
        }

        sb.append("]");
        return sb.toString();

    }


    /**
     * entry 里面有是Object 类型的key and value
     * 调用style(Object value)方法
     *
     * @param entry 要风格化的entry
     * @return 风格化后的字符串
     */
    private String style(Map.Entry<?, ?> entry) {
        return style(entry.getKey()) + " -> " + style(entry.getValue());
    }

    private String style(Collection<?> coll) {
        StringBuilder sb = new StringBuilder(coll.size() * 8 + 16);

        sb.append(getCollectionTypeString(coll)).append("[");
        for (Iterator<?> it = coll.iterator(); it.hasNext(); ) {
            Object next = it.next();
            sb.append(next);
            if (it.hasNext()) {
                sb.append(',').append(' ');
            }


        }
        if (coll.isEmpty()) {
            sb.append(EMPTY);
        }
        sb.append("]");
        return sb.toString();
    }

    //获取集合里面的元素的类型
    private String getCollectionTypeString(Collection<?> coll) {
        if (coll instanceof List) {
            return LIST;
        } else if (coll instanceof Set) {
            return SET;
        } else {
            return COLLECTION;
        }

    }

    private String styleArray(Object[] array) {
        StringBuilder sb = new StringBuilder(array.length + 16);
        sb.append(ARRAY + "<")
                .append(ClassUtils.getShortName(array.getClass().getComponentType()))
                .append(">[");

        for (int i = 0; i < array.length - 1; i++) {
            sb.append(style(array[i]));
            sb.append(',').append(' ');
        }

        if (array.length > 0) {
            sb.append(style(array[array.length - 1])).append("]");
        } else {
            sb.append(EMPTY);
        }


        return sb.toString();


    }
}
