package com.luo.util;

import com.luo.lang.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {


    //-----default attrs
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;

    //指定默认的引用类型
    private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;

    private static final int MAXIMUM_CONCRRENCY_LEVEL = 1 << 16;
    private static final int MAXIMUM_SEGMENT_SIZE = 1 << 30;

    //attrs

    private final Segment[] segments;

    private final float loadFactor;

    private final ReferenceType referenceType;


    //the shift value of the num of the segments
    private final int shift;

    //late binding entry set
    private Set<Map.Entry<K, V>> entrySet;


    //----------constructors:5个

    public ConcurrentReferenceHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, DEFAULT_CONCURRENCY_LEVEL, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType referenceType) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL, referenceType);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    /**
     * 4个参数
     *
     * @param initialCapacity  the initial capacity of the map
     * @param loadFactor
     * @param concurrencyLevel the expected num of threads that will concurrently write to the map
     * @param referenceType    the reference type used for the entry
     */
    @SuppressWarnings("unchecked")
    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {

        //-1 pre check
        Assert.isTrue(initialCapacity > 0, "initial capacity must not be negative");
        Assert.isTrue(loadFactor > 0f, "load factor must  be positive");
        Assert.isTrue(concurrencyLevel > 0f, "concurrency level  must  be positive");
        Assert.notNull(referenceType, "reference type must not be null");
        this.loadFactor = loadFactor;
        this.shift = calculateShift(concurrencyLevel, MAXIMUM_CONCRRENCY_LEVEL);
        int size = 1 << this.shift;//obtain a value
        this.referenceType = referenceType;
        //获得四舍五入的segment 容量
        int roundedUpSegmentCapacity = (int) ((initialCapacity + size - 1L) / size);
        int initialSize = 1 << calculateShift(roundedUpSegmentCapacity, MAXIMUM_SEGMENT_SIZE);
        //add uncheck
        Segment[] segments = (Segment[]) Array.newInstance(Segment.class, size);
        int resizeThreshold = (int) (initialSize * getLoadFactor());
        //初始化每个segment
        for (int i = 0; i < segments.length; i++) {
            segments[i] = new Segment(initialSize, resizeThreshold);
        }
        this.segments = segments;//赋值


    }


    /**
     * calculate the shift value between the min and max
     *
     * @param minimum min value
     * @param maximum max value
     * @return the shift value
     */
    protected static int calculateShift(int minimum, int maximum) {
        //initialize the shift first
        int shift = 0;
        int value = 1;
        while (value < minimum && value < maximum) {
            value <<= 1;
            shift++;
        }
        return shift;
    }
    protected ReferenceManager createReferenceManager() {
        return new ReferenceManager();
    }

    //分割map的块
    @SuppressWarnings("serial")
    protected final class Segment extends ReentrantLock {


        private final ReferenceManager referenceManager;

        private final int initialSize;

        private volatile Reference<K, V>[] references;

        //the total num of ref in the segment,including the refs that not purged
        private volatile int count = 0;

        private int resizeThreshold;


        public Segment(int initialSize, int resizeThreshold) {
            //调用ConcurrentReferenceHashMap的方法
            this.referenceManager = createReferenceManager();
            this.initialSize = initialSize;
            this.references = createReferenceArray(initialSize);//初始化ref表
            this.resizeThreshold = resizeThreshold;
        }

        @Nullable
        public Reference<K, V> getReference(@Nullable Object key, int hash, Restructure restructure) {

            if (restructure == Restructure.WHEN_NECESSARY) {
                restructureIfNecessary(false);
            }

            if (this.count == 0) {
                return null;
            }

            Reference<K, V>[] references = this.references;
            int index = getIndex(hash, references);
            Reference<K, V> head = references[index];

            return findInChain(head, key, hash);

        }


        /**
         * update the segment
         *
         * @param hash the hash of the key
         * @param key
         * @param task the update operation
         * @param <T>  the result of the task
         * @return the result of the task
         */
        public <T> T doTask(int hash, Object key, Task<T> task) {
            //-1 是否需要进行扩容操作
            boolean resize = task.hasOption(TaskOption.RESIZE);

            if (task.hasOption(TaskOption.RESTRUCTURE_BEFORE)) {
                //-2 进行重构
                restructureIfNecessary(resize);
            }

            //-3 如果sement的容量是空
            if (task.hasOption(TaskOption.SKIP_IF_EMPTY) && this.count == 0) {
                return task.execute(null, null, null);//没有更新操作
            }

            lock();

            try {
                //根据key的hash值 获取ref的索引
                final int index = getIndex(hash, this.references);
                //根据index获取ref:队列
                Reference<K, V> head = this.references[index];
                Reference<K, V> ref = findInChain(head, key, hash);
                //获得entry
                Entry<K, V> entry = (ref != null ? ref.get() : null);

                //创建Entries
                Entries entries = new Entries() {
                    //重写add方法
                    @Override
                    public void add(@Nullable V value) {
                        @SuppressWarnings("unchecked")
                        //创建entry
                                Entry<K, V> newEntry = new Entry((K) key, value);
                        Reference<K, V> newReference = Segment.this.referenceManager.createReference(newEntry, hash, head);
                        Segment.this.references[index] = newReference;//新增一个Ref
                        Segment.this.count++;


                    }
                };
                return task.execute(ref, entry, entries);

            } finally {
                unlock();
                if (task.hasOption(TaskOption.RESTRUCTURE_AFTER)) {
                    restructureIfNecessary(resize);
                }
            }

        }

        //clear the segment
        public void clear() {
            if (this.count == 0) {
                return;//没有容量，不需要清空
            }
            lock();

            try {
                this.references = createReferenceArray(initialSize);
                resizeThreshold = (int) (this.references.length * getLoadFactor());
                this.count = 0;
            } finally {
                unlock();
            }
        }

        /**
         * 重构引用表的数据结构：扩容以及清除被垃圾收集器处理的引用
         *
         * @param allowResize 是否允许扩容
         */
        protected void restructureIfNecessary(boolean allowResize) {

            //如果容量大于等于阈值，就需要扩容
            boolean needsResize = (this.count > 0 && this.count >= this.resizeThreshold);

            Reference<K, V> ref = this.referenceManager.pollForPurge();

            if (ref != null || (needsResize && allowResize)) {
                lock();//调用重入锁的lock方法

                try {
                    //重构之后的ref数量
                    int countAfterRestructure = this.count;
                    Set<Reference<K, V>> toPurge = Collections.emptySet();
                    if (ref != null) {
                        toPurge = new HashSet<>();

                        while (ref != null) {
                            toPurge.add(ref);
                            ref = this.referenceManager.pollForPurge();

                        }
                    }
                    //已经清理完全部的引用，在对countAfterRestructure重新赋值
                    countAfterRestructure -= toPurge.size();

                    //清理完之后，再判断是否需要进行扩容
                    needsResize = (countAfterRestructure > 0 && countAfterRestructure >= this.resizeThreshold);
                    //是否已经扩容的标签
                    boolean resizing = false;

                    int restructureSize = this.references.length;

                    if (allowResize && needsResize && restructureSize > MAXIMUM_SEGMENT_SIZE) {
                        restructureSize <<= 1;//扩容
                        resizing = true;
                    }
                    //不满足上面的条件，那么久创建createReferenceArray一个新的表，或使用原来存在的表:this.references table
                    //restructured 表示已经重构的ref table
                    Reference<K, V>[] restructured =
                            (resizing ? createReferenceArray(restructureSize) : this.references);

                    //开始重构
                    for (int i = 0; i < this.references.length; i++) {
                        ref = this.references[i];
                        if (!resizing) {
                            restructured[i] = null;
                        }

                        while (ref != null) {
                            if (!toPurge.contains(ref)) {//对没有被GC收集的引用进行重构
                                Entry<K, V> entry = ref.get();
                                if (entry != null) {
                                    //根据ref的hash值获取他所在restructured数组里面的索引
                                    int index = getIndex(ref.getHash(), restructured);
                                    restructured[index] = this.referenceManager.createReference(entry, ref.getHash(), restructured[index]);

                                }
                            }
                            ref = ref.getNext();//对下一个ref进行重构
                        }


                    }// end for
                    //// Replace volatile members

                    if (resizing) {
                        this.references = restructured;
                        //更新阈值
                        this.resizeThreshold = (int) (this.references.length * getLoadFactor());
                    }
                    //不论扩容不扩容，现在都要更新容量
                    this.count = Math.max(countAfterRestructure, 0);//数量应该大于等于0


                } finally {
                    unlock();
                }

            }


        }

        //根据给出的ref 和他所在表的hash以及这个ref里面的entry的key来查找ref
        @Nullable
        private Reference<K, V> findInChain(Reference<K, V> ref, Object key, int hash) {
            Reference<K, V> currRef = ref;
            while (currRef != null) {
                if (currRef.getHash() == hash) {
                    Entry<K, V> entry = currRef.get();
                    if (entry != null) {
                        K entryKey = entry.getKey();
                        if (ObjectUtils.nullSafeEquals(entryKey, key)) {
                            return currRef;
                        }
                    }
                }
                currRef = currRef.getNext();
            }
            return null;

        }

        private int getIndex(int hash, Reference<K, V>[] references) {
            return (hash & (references.length - 1));
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private Reference<K, V>[] createReferenceArray(int size) {
            return new Reference[size];
        }

        public int getSize() {
            return this.references.length;
        }

        public int getCount() {
            return this.count;
        }

    }
    //--------end Segment

    protected final float getLoadFactor() {
        return this.loadFactor;
    }

    //--------ReferenceManager
    protected class ReferenceManager {

        private final ReferenceQueue<Entry<K, V>> queue = new ReferenceQueue<>();

        /**
         * this is a factory method creating a Reference(interface)
         *
         * @param entry the entry contained in this reference
         * @param hash
         * @param next
         * @return
         */
        public Reference<K, V> createReference(Entry<K, V> entry, int hash, Reference<K, V> next) {

            if (ConcurrentReferenceHashMap.this.referenceType == ReferenceType.WEAK) {
                return new WeakEntryReference<>(entry, hash, next, this.queue);
            }
            return new SoftEntryReference<>(entry, hash, next, this.queue);

        }

        //返回被清理的引用，可能为空
        @Nullable
        public Reference<K, V> pollForPurge() {
            this.queue.poll();
            return (Reference<K, V>) this.queue.poll();

        }

    }

    //soft entry reference 实现
    private static final class SoftEntryReference<K, V>
            extends SoftReference<Entry<K, V>> implements Reference<K, V> {

        private final int hash;

        private final Reference<K, V> nextReference;

        public SoftEntryReference(Entry<K, V> entry, int hash, @Nullable Reference<K, V> next,
                                  ReferenceQueue<Entry<K, V>> queue) {
            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;
        }

        @Override
        public int getHash() {
            return this.hash;
        }

        @Override
        @Nullable
        public Reference getNext() {
            return this.nextReference;
        }

        @Override
        public void release() {
            enqueue();
            clear();
        }
    }


    //weak entry reference 实现
    private static final class WeakEntryReference<K, V>
            extends WeakReference<Entry<K, V>> implements Reference<K, V> {

        private final int hash;

        private final Reference<K, V> nextReference;

        public WeakEntryReference(Entry<K, V> entry, int hash, @Nullable Reference<K, V> next,
                                  ReferenceQueue<Entry<K, V>> queue) {

            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;

        }

        @Override
        public int getHash() {
            return this.hash;
        }

        @Override
        @Nullable
        public Reference getNext() {
            return this.nextReference;
        }

        @Override
        public void release() {
            enqueue();
            clear();
        }
    }


    //Reference
    protected interface Reference<K, V> {

        @Nullable
        Entry<K, V> get();

        int getHash();//reference hash

        //the next reference in the chain
        @Nullable
        Reference<K, V> getNext();

        //release the entry
        void release();

    }

    //entry 实现
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


    private abstract class Task<T> {
        private final EnumSet<TaskOption> options;

        public Task(TaskOption... options) {
            this.options = (options.length == 0 ? EnumSet.noneOf(TaskOption.class) : EnumSet.of(options[0], options));
        }

        public boolean hasOption(TaskOption option) {
            return this.options.contains(option);
        }

        @Nullable
        protected T execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry, @Nullable Entries entries) {
            return execute(ref, entry);
        }

        /**
         * do not need Entries
         *
         * @param ref   the fount ref
         * @param entry the found entry
         * @return the result ofthe task
         */
        @Nullable
        protected T execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry) {
            return null;
        }
    }


    private abstract class Entries {
        @Nullable
        public abstract void add(@Nullable V value);
    }

    /**
     * Various options
     */
    private enum TaskOption {

        RESTRUCTURE_BEFORE, RESTRUCTURE_AFTER, SKIP_IF_EMPTY, RESIZE
    }

    //枚举类
    static enum Restructure {
        WHEN_NECESSARY,
        NEVER
    }

    //枚举类
    static enum ReferenceType {
        //use java SoftReference
        SOFT,
        //use java WeakReference
        WEAK
    }
}
