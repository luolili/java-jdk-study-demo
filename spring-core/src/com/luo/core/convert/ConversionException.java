package com.luo.core.convert;

import com.luo.core.NestRuntimeException;

/**
 * exception for convsersion system
 */
public class ConversionException extends NestRuntimeException {


    public ConversionException(String msg) {
        super(msg);
    }

    public ConversionException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
