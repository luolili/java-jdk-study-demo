package com.luo.core;

import com.luo.lang.Nullable;
import com.sun.istack.internal.NotNull;

public class NestedExceptionUtils {


    @Nullable
    public static String buildMessage(@Nullable String message, @Nullable Throwable cause) {

        if (cause == null) {
            return message;
        }
        StringBuilder sb = new StringBuilder(64);
        if (message != null) {
            sb.append(message).append("; ");
        }
        sb.append("nest exception is : ").append(cause);
        return sb.toString();

    }

    public static Throwable getRootCause(@Nullable Throwable original) {
        if (original == null) {
            return null;
        }
        //准备root cause
        Throwable rootCause = null;
        Throwable cause = original.getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            cause = cause.getCause();
        }

        return rootCause;
    }

    //most specific cause is either the root cause or itself
    public static Throwable getMostSpecificCause(@Nullable Throwable original) {
        Throwable rootCause = getRootCause(original);
        return (rootCause != null ? rootCause : original);


    }
}
