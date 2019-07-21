package com.luo.core.convert;

import com.luo.lang.Nullable;
import com.luo.util.ObjectUtils;

/**
 * exception for failed type conversion attempt
 */

@SuppressWarnings("serial")
public class ConversionFailedException extends ConversionException {

    @Nullable
    private final TypeDescriptor sourceType;
    private final TypeDescriptor targetType;

    @Nullable
    private final Object value;

    public ConversionFailedException(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType,
                                     @Nullable Object value, Throwable cause) {

        super("Failed to convert from type [" + sourceType + "] to type [" + targetType +
                "] for value '" + ObjectUtils.nullSafeToString(value) + "'", cause);
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.value = value;
    }

    public TypeDescriptor getSourceType() {
        return sourceType;
    }

    public TypeDescriptor getTargetType() {
        return targetType;
    }

    public Object getValue() {
        return value;
    }
}
