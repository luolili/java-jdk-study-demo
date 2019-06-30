package com.luo.core;

import com.luo.lang.Nullable;
import com.luo.util.ConcurrentReferenceHashMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.*;

final class SerializableTypeWrapper {

    private static final Class<?>[] SUPPORTED_SERIALIZABLE_TYPES = {
            GenericArrayType.class, ParameterizedType.class, TypeVariable.class, WildcardType.class
    };

    //use ConcurrentReferenceHashMap to hold the type
    static final ConcurrentReferenceHashMap<Type, Type> cache = new ConcurrentReferenceHashMap<>(256);

    //私有化构造方法
    private SerializableTypeWrapper() {

    }

    @SuppressWarnings("serial")
    static class FieldTypeProvider implements TypeProvider {

        private final String fieldName;//字段的名字
        private final Class<?> declaringClass;//字段所在的类
        private transient Field field;

        public FieldTypeProvider(Field field) {
            this.fieldName = field.getName();
            this.declaringClass = field.getDeclaringClass();
            this.field = field;
        }

        @Override
        public Type getType() {
            return this.field.getGenericType();
        }

        @Override
        public Object getSource() {
            return this.field;
        }

        private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException, NoSuchFieldException {
            inputStream.defaultReadObject();
            //通过字段的名字获取字段对象
            try {
                this.field = this.declaringClass.getDeclaredField(this.fieldName);
            } catch (Throwable ex) {
                throw new IllegalStateException("Could not find original class structure", ex);
            }
        }
    }

    @SuppressWarnings("serial")
    interface TypeProvider extends Serializable {

        @Nullable
        Type getType();

        //return the source of the type
        @Nullable
        default Object getSource() {
            return null;
        }
    }

}
