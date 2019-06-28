package com.luo.core.style;

import com.luo.lang.Nullable;
import com.luo.util.Assert;

/**
 * 完成定义在策略接口中的方法
 */
public class ToStringCreator {


    //准备默认的toString styler, final
    static final ToStringStyler DEFAULT_TO_STRING_STYLER =
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

    //--append
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
}
