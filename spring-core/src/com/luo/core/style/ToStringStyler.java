package com.luo.core.style;

import com.luo.lang.Nullable;

/**
 * 目的：完成一个输出友好的打印骨架，
 * 是一个策略接口。他需要一个比如builder的实现类来实现这个流程:workflow
 */
public interface ToStringStyler {


    /**
     * 在风格化对象的字段之前先风格化toString 的对象
     *
     * @param buffer 输出的结果
     * @param obj    要风格化的对象
     */
    void styleStart(StringBuilder buffer, Object obj);

    void styleEnd(StringBuilder buffer, Object obj);

    //风格化字段

    /**
     * @param buffer
     * @param filedName 字段的名称
     * @param value     字段的值
     */
    void styleField(StringBuilder buffer, String filedName, @Nullable Object value);

    //风格化给定的对象
    void styleValue(StringBuilder buffer, Object value);

    //字段之间的分隔符
    void styleFieldSeparator(StringBuilder buffer);


}
