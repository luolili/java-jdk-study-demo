package com.luo.util;

import java.lang.reflect.Type;

/**
 * generic type param
 */
public abstract class TypeUtils {


    public static boolean isAssignable(Type lhsType, Type rhsType) {
        //all types are assignable to themselves and class Pbject
        if (lhsType.equals(rhsType) || Object.class == lhsType) {
            return true;
        }


        return false;
    }
}
