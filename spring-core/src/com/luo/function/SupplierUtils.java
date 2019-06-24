package com.luo.function;

import com.luo.lang.Nullable;

import java.util.function.Supplier;

public abstract class SupplierUtils {
    @Nullable
    public static <T> T resolve(Supplier<T> supplier) {

        return (supplier != null ? supplier.get() : null);
    }
}
