package com.luo.util;

import com.luo.lang.Nullable;

import java.lang.ref.ReferenceQueue;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {


    //attrs
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;

    //指定默认的引用类型
    private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;

    private static final int MAXIMUM_CONCRRENCY_LEVEL = 1 << 16;
    private static final int MAXIMUM_SEGMENT_SIZE = 1 << 30;


    //分割map的块
    protected static class Segment extends ReentrantLock {


    }

    protected class ReferenceManager {
        private final ReferenceQueue<E> new ReferenceQueue<>();
    }

    //entry
    protected static final class Entry<K, V> implements Map.Entry<K, V> {

        @Nullable
        private final K key;

        @Nullable
        private volatile V value;

        //构造方法
        public Entry(@Nullable K key, @Nullable V value) {
            this.key = key;
            this.value = value;
        }

        //重写get方法：getKey and getValue
        @Override
        @Nullable
        public K getKey() {
            return this.key;
        }

        @Override
        @Nullable
        public V getValue() {
            return this.value;
        }
        //重写set方法:key的定义是final索引不能提供setKey


        @Override
        @Nullable
        public V setValue(@Nullable V value) {
            //设置新值之前的旧值
            V previous = this.value;
            this.value = value;
            return previous;

        }
        //to string


        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }

        //equals
        @Override//此方法是不可被继承修改的
        @SuppressWarnings("rawtypes")
        public final boolean equals(Object other) {
           /* if (other == null) {
                return false;
            }*/
            //-1 内存地址判断
            if (this == other) {
                return true;
            }

            //-2 类型判断
            if (!(other instanceof Map.Entry)) {
                return false;
            }

            //-3 类型转换
            Map.Entry otherEntry = (Map.Entry) other;
            return ObjectUtils.nullSafeEquals(getKey(), otherEntry.getKey())
                    && ObjectUtils.nullSafeEquals(getValue(), otherEntry.getValue());

        }

        //重写hashCode方法
        @Override
        public int hashCode() {
            return ObjectUtils.nullSafeHashCode(this.key) ^ ObjectUtils.nullSafeHashCode(this.value);
        }
    }


    //枚举类
    static enum ReferenceType {
        //use java SoftReference
        SOFT,
        //use java WeakReference
        WEAK
    }
}
