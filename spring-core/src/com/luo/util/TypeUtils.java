package com.luo.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * generic type param
 */
public abstract class TypeUtils {


    public static boolean isAssignable(Type lhsType, Type rhsType) {
        //all types are assignable to themselves and class object
        if (lhsType.equals(rhsType) || Object.class == lhsType) {
            return true;
        }
        //condition 1: left is Class type
        /**
         * for right:
         * 1. right is also aClass type-->use ClassUtils.isAssignable
         * 2. right is ParameterizedType type:
         * if the type is Class type-->use ClassUtils.isAssignable
         * 3.if left is an array and right is GenericArrayType type
         * right.getGenericComponentType -- > return Type
         * last, invoke itself
         */
        if (lhsType instanceof Class) {
            Class<? extends Type> lhsClass = lhsType.getClass();
            if (rhsType instanceof Class) {
                Class<? extends Type> rhsClass = rhsType.getClass();
                return ClassUtils.isAssignable(lhsClass, rhsClass);

            }

            if (rhsType instanceof ParameterizedType) {
                Type rhsRaw = ((ParameterizedType) rhsType).getRawType();
                //convert it to Class type
                if (rhsRaw instanceof Class) {
                    return ClassUtils.isAssignable(lhsClass, (Class<?>) rhsRaw);
                }

            } else if (lhsClass.isArray() && rhsType instanceof GenericArrayType) {
                Type rhsComponent = ((GenericArrayType) rhsType).getGenericComponentType();

                //invoke itself
                isAssignable(((Class) lhsType).getComponentType(), rhsComponent);
            }

        }
        //---

        //condition 2: left is ParameterizedType type
        /**
         * for right:
         * 1. right is Class type, left.getRawType return Type
         * if this Type is Class type -->ClassUtils.isAssignable
         * 2.right is ParameterizedType type, right.getRawType return Type
         * invoke itself
         */
        if (lhsType instanceof ParameterizedType) {
            if (rhsType instanceof Class) {
                Type lhsRaw = ((ParameterizedType) lhsType).getRawType();
                if (lhsRaw instanceof Class) {
                    return ClassUtils.isAssignable((Class<?>) lhsRaw, (Class<?>) rhsType);
                }

            } else if (rhsType instanceof ParameterizedType) {
                Type rhsRaw = ((ParameterizedType) rhsType).getRawType();
                Type lhsRaw = ((ParameterizedType) lhsType).getRawType();
                // //invoke itself
                isAssignable(lhsRaw, rhsRaw);

            }
        }


        //condition 3 left is GenericArrayType
        /**
         * first left.getGenericComponentType return Type
         * for right:
         * 1. right is Class Type, if right is an array, rhsClass.getComponentType
         * invoke itself
         * 2. right is GenericArrayType type too, right.getGenericComponentType
         * invoke itself
         */
        if (lhsType instanceof GenericArrayType) {
            Type lhsComponent = ((GenericArrayType) lhsType).getGenericComponentType();
            if (rhsType instanceof Class) {
                Class rhsClass = (Class) rhsType;

                if (rhsClass.isArray()) {
                    isAssignable(lhsComponent, rhsClass.getComponentType());
                }
            } else if (rhsType instanceof GenericArrayType) {
                Type rhsComponent = ((GenericArrayType) rhsType).getGenericComponentType();
                isAssignable(lhsComponent, rhsComponent);

            }
        }

        //condition 4 left is WildcardType
        if (lhsType instanceof WildcardType) {
            isAssignable((WildcardType) lhsType, rhsType);
        }
        return false;
    }

    private static boolean isAssignable(ParameterizedType lhsType, ParameterizedType rhsType) {
        //-1 referrence equals
        if (lhsType.equals(rhsType)) {
            return true;
        }

        Type[] lhsTypeArguments = lhsType.getActualTypeArguments();
        Type[] rhsTypeArguments = rhsType.getActualTypeArguments();
        if (lhsTypeArguments.length != rhsTypeArguments.length) {
            return false;
        }

        for (int i = 0; i < lhsTypeArguments.length; i++) {
            Type lhsTypeArgument = lhsTypeArguments[i];
            Type rhsTypeArgument = rhsTypeArguments[i];

            if (!lhsTypeArgument.equals(rhsTypeArgument) &&
                    !(lhsTypeArgument instanceof WildcardType && isAssignable((WildcardType) lhsTypeArgument, rhsTypeArgument))) {
                return false;
            }


        }
        return true;
    }
}
