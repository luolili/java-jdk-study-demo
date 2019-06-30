package com.luo.core.convert;

/**
 * exception for failed type conversion attempt
 */
public class ConversionFailedException extends ConversionException {


    public ConversionFailedException(String msg) {
        super(msg);
    }

    public ConversionFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
