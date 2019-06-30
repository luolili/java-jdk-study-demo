package com.luo.core;

/**
 * Graal的图片环境的检测的代理
 */
abstract class GraalDetector {
    private static final boolean imageCode = (System.getProperty("org.graalvm.nativeimage.imagecode") != null);


    /**
     * Return whether this runtime environment lives within a native image.
     */
    public static boolean inImageCode() {
        return imageCode;
    }

}
