package com.luo.core.style;

import com.luo.lang.Nullable;
import com.luo.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * 将对象转换为字符串的风格
 */
public class DefaultValueStyler implements ValueStyler {
    //attr ,tag
    private static final String EMPTY = "[empty]";
    private static final String NULL = "[null]";
    private static final String COLLECTION = "[collection]";
    private static final String SET = "[set]";
    private static final String LIST = "[list]";
    private static final String MAP = "[map]";
    private static final String ARRAY = "[array]";

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
        }


        return null;
    }


    private <K, V> String style(Map<K, V> map) {

        //-1 创建结果
        StringBuilder sb = new StringBuilder(map.size() * 8 + 16);

        sb.append(MAP + "[");

        for (Iterator<Map.Entry<K, V>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<K, V> entry = it.next();
            sb.append(style(entry));//调用的是style(Object value)
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
}
