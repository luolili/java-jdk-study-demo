package com.luo.core.style;

public class StylerUtils {
    //准备2个是tyler的默认实现的实力对象

    static ValueStyler DEFAULT_VALUE_STYLER = new DefaultValueStyler();


    //style the given value by the spring default convention
    public static String style(Object value) {
        return DEFAULT_VALUE_STYLER.style(value);
    }
}
