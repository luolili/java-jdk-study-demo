package com.luo.core.style;

import com.luo.lang.Nullable;
import com.luo.util.Assert;

/**
 * 完成定义在策略接口中的方法
 */
public class ToStringCreator {


    //准备默认的toString styler, final
    private static final ToStringStyler DEFAULT_TO_STRING_STYLER =
            new DefaultToStringStyler(StylerUtils.DEFAULT_VALUE_STYLER);


    private final StringBuilder buffer = new StringBuilder(256);

    private final ToStringStyler styler;

    private final Object object;


    private boolean styledFirstField;

    public ToStringCreator(Object obj) {
        this(obj, (ToStringStyler) null);
    }

    public ToStringCreator(Object obj, @Nullable ValueStyler styler) {
        //调用下面的构造方法
        this(obj, new DefaultToStringStyler((styler != null ? styler : StylerUtils.DEFAULT_VALUE_STYLER)));
    }

    public ToStringCreator(Object obj, ToStringStyler toStringStyler) {
        Assert.notNull(obj, "obj to be styled must not be null");
        this.object = obj;
        this.styler = (toStringStyler != null ? toStringStyler : DEFAULT_TO_STRING_STYLER);
        this.styler.styleStart(this.buffer, this.object);//开始风格化

    }


    //--7个基本类型转为Object, char 不用转
    public ToStringCreator append(String fielfName, byte value) {
        return append(fielfName, Byte.valueOf(value));
    }

    public ToStringCreator append(String fielfName, short value) {
        return append(fielfName, Short.valueOf(value));
    }

    public ToStringCreator append(String fielfName, int value) {
        return append(fielfName, Integer.valueOf(value));
    }

    public ToStringCreator append(String fielfName, float value) {
        return append(fielfName, Float.valueOf(value));
    }

    public ToStringCreator append(String fielfName, long value) {
        return append(fielfName, Long.valueOf(value));
    }

    public ToStringCreator append(String fielfName, double value) {
        return append(fielfName, Double.valueOf(value));
    }

    public ToStringCreator append(String fielfName, boolean value) {
        return append(fielfName, Boolean.valueOf(value));
    }


    //--append 基础方法
    public ToStringCreator append(String fieldName, @Nullable Object value) {
        printFieldSeparatorIfNeccsary();
        this.styler.styleField(this.buffer, fieldName, value);
        return this;
    }

    private void printFieldSeparatorIfNeccsary() {
        if (this.styledFirstField) {
            this.styler.styleFieldSeparator(this.buffer);
        } else {
            this.styledFirstField = true;
        }
    }

    public ToStringCreator append(Object value) {
        this.styler.styleValue(this.buffer, value);
        return this;
    }

    @Override
    public String toString() {
        this.styler.styleEnd(this.buffer, this.object);
        return this.buffer.toString();
    }
}
