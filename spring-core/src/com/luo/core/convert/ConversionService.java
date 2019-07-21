package com.luo.core.convert;

import com.luo.lang.Nullable;

/**
 * 类型转换的服务接口，thread-safe
 */
public interface ConversionService {

    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType);

    //方法上的泛型使用
    @Nullable
    <T> T convert(@Nullable Object source, Class<T> targetType);


    Object convert(@Nullable Object source, TypeDescriptor sourceType,
                   TypeDescriptor targetType);
}
