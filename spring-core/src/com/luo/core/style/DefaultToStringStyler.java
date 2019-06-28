package com.luo.core.style;

import com.luo.util.Assert;
import com.luo.util.ClassUtils;
import com.luo.util.ObjectUtils;

/**
 *
 */
public class DefaultToStringStyler implements ToStringStyler {
    //to string styler needs value styler
    private ValueStyler valueStyler;

    public DefaultToStringStyler(ValueStyler valueStyler) {
        Assert.notNull(valueStyler, "value styler must not be null");
        this.valueStyler = valueStyler;
    }

    protected final ValueStyler getValueStyler() {

        return this.valueStyler;
    }

    //--


    @Override
    public void styleStart(StringBuilder buffer, Object obj) {
        if (!obj.getClass().isArray()) {
            buffer.append("[")
                    .append(ClassUtils.getShortName(obj.getClass()));
            styleIdentityHashCode(buffer, obj);
        } else {
            buffer.append("[");
            styleIdentityHashCode(buffer, obj);

            buffer.append(' ');
            styleValue(buffer, obj);
        }
    }

    @Override
    public void styleEnd(StringBuilder buffer, Object obj) {
        buffer.append("]");
    }

    @Override
    public void styleField(StringBuilder buffer, String filedName, Object value) {
        styleFieldStart(buffer, filedName);
        styleValue(buffer, value);
        styleFieldEnd(buffer, filedName);
    }

    protected void styleFieldStart(StringBuilder buffer, String fieldName) {
        buffer.append(' ').append(fieldName).append(" = ");
    }

    protected void styleFieldEnd(StringBuilder buffer, String fieldName) {
    }

    @Override
    public void styleValue(StringBuilder buffer, Object value) {

        buffer.append(this.valueStyler.style(value));
    }

    @Override
    public void styleFieldSeparator(StringBuilder buffer) {
        buffer.append(',');
    }

    private void styleIdentityHashCode(StringBuilder buffer, Object obj) {
        buffer.append("@");
        buffer.append(ObjectUtils.getIdentityHexString(obj));
    }
}
