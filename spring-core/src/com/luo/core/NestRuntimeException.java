package com.luo.core;

import com.luo.lang.Nullable;

/**
 * handy class：包装RuntimeException
 * 他是抽象类，这强制开发人必须继承他
 */
public abstract class NestRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5439915454935047936L;

    static {
        //防止类加载器的死锁情况，这里使用eagerly load 这个根据类
        NestedExceptionUtils.class.getName();
    }

    public NestRuntimeException(String msg) {
        super(msg);//调用父类的构造方法
    }

    public NestRuntimeException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    /**
     * @return 返回父类的错误信息和原因
     */
    @Override
    @Nullable
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }

    @Nullable
    public Throwable getRootCause() {
        return NestedExceptionUtils.getRootCause(this);//this can't exist in the static context
    }

    public Throwable getMostSpecificCause() {
        Throwable rootCause = getRootCause();
        return (rootCause != null ? rootCause : this);

    }

    public boolean contains(Class<?> exType) {
        if (exType == null) {
            return false;
        }

        //
        if (exType.isInstance(this)) {
            return true;
        }

        Throwable cause = getCause();
        if (cause == this) {
            return false;
        }


        if (cause instanceof NestRuntimeException) {
            return ((NestRuntimeException) cause).contains(exType);
        } else {
            while (cause != null) {
                if (exType.isInstance(cause)) {
                    return true;
                }
                if (cause.getCause() == cause) {
                    break;
                }
                cause = cause.getCause();//循环
            }
            return false;
        }

    }



}
