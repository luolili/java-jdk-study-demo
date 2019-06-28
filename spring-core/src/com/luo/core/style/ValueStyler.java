package com.luo.core.style;

import com.luo.lang.Nullable;

/**
 * 定义策略
 */

public interface ValueStyler {


    //return the styled string
    String style(@Nullable Object value);
}

