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


    //----------constructors:6个

    public ConcurrentReferenceHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL, DEFAULT_REFERENCE_TYPE);
    }

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


    public final int getSegmentsSize() {
        return this.segments.length;
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
    public final class Segment extends ReentrantLock {

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
                //初始化references, resizeThreshold, count
                this.references = createReferenceArray(initialSize);
                resizeThreshold = (int) (this.references.length * getLoadFactor());
                this.count = 0;
            } finally {
                unlock();
            }
        }

        /**
         * 重构引用表的数据结构：扩容以及清除被垃圾收集器处理的引用
         *当allowResize为false的时候，不会扩容，只会新建一个表，或者用原来的表
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
                    //不满足上面的条件，那么创建createReferenceArray一个新的表，或使用原来存在的表:this.references table
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

        //根据给出的ref 和key所在表的hash以及这个ref里面的entry的key来查找ref
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

    public final float getLoadFactor() {
        return this.loadFactor;
    }

    protected final int getMaximumSegmentSize() {

        return this.segments.length;
    }

    public final Segment getSegment(int index) {
        return this.segments[index];
    }

    protected int getHash(@Nullable Object o) {
        int hash = (o != null ? o.hashCode() : 0);
        hash += (hash << 15) ^ 0xffffcd7d;
        hash ^= (hash >>> 10);
        hash += (hash << 3);
        hash ^= (hash >>> 6);
        hash += (hash << 2) + (hash << 14);
        hash ^= (hash >>> 16);
        return hash;
    }

    private Segment getSegmentForHash(int hash) {
        return this.segments[(hash >>> (32 - this.shift)) & (this.segments.length - 1)];
    }

    /**
     * 通过key 和Restructure获取Ref
     *
     * @param key
     * @param restructure
     * @return ref
     */
    @Nullable
    public final Reference<K, V> getReference(Object key, Restructure restructure) {
        int hash = getHash(key);
        return getSegmentForHash(hash).getReference(key, hash, restructure);

    }

    @Nullable
    private Entry<K, V> getEntryIfAvailable(@Nullable Object key) {
        Reference<K, V> reference = getReference(key, Restructure.WHEN_NECESSARY);
        return (reference != null ? reference.get() : null);
    }

    //-----重写map里面的方法


    @Override
    @Nullable//获得entry的value
    public V get(Object key) {
        Entry<K, V> entry = getEntryIfAvailable(key);
        return (entry != null ? entry.getValue() : null);
    }

    @Override
    @Nullable//获得entry的value
    public V getOrDefault(Object key, V defaultValue) {
        Entry<K, V> entry = getEntryIfAvailable(key);
        return (entry != null ? entry.getValue() : defaultValue);
    }

    @Override
    public boolean containsKey(Object key) {
        Entry<K, V> entry = getEntryIfAvailable(key);
        return (entry != null && ObjectUtils.nullSafeEquals(entry.getKey(), key));


    }

    //---put

    private <T> T doTask(Object key, Task<T> task) {
        int hash = getHash(key);
        return getSegmentForHash(hash).doTask(hash, key, task);
    }

    /**
     * 只有该类可以调用这个方法
     *
     * @param key
     * @param value
     * @param overwriteExisting
     * @return
     */
    private V put(@Nullable final K key, @Nullable final V value, final boolean overwriteExisting) {
        return doTask(key, new Task<V>(TaskOption.RESTRUCTURE_BEFORE, TaskOption.RESIZE) {
            @Override
            @Nullable
            protected V execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry, @Nullable Entries entries) {
                if (entry != null) {
                    V oldValue = entry.getValue();
                    if (overwriteExisting) {
                        entry.setValue(value);
                    }
                    return oldValue;
                }
                //判空
                Assert.state(entries != null, "no entries segment");
                entries.add(value);
                return null;

            }
        });
    }

    @Override
    public V put(K key, V value) {
        return put(key, value, true);
    }

    @Override
    @Nullable//from map
    public V putIfAbsent(K key, V value) {
        return put(key, value, false);
    }

    @Override
    @Nullable
    public V remove(Object key) {
        return doTask(key, new Task<V>(TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY) {

            @Override
            protected V execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry) {
                if (entry != null) {
                    if (ref != null) {
                        ref.release();
                    }
                    return entry.value;
                }
                return null;
            }
        });
    }


    //删除给出的key和value值，如果key对应的value值不等于给出的value，返回false
    @Override
    public boolean remove(Object key, final Object value) {
        Boolean result = doTask(key, new Task<Boolean>(TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY) {

            @Override
            protected Boolean execute(Reference<K, V> ref, Entry<K, V> entry) {
                if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), value)) {
                    if (ref != null) {
                        ref.release();
                    }
                    return true;
                }
                return false;
            }
        });
        return (result == Boolean.TRUE);
    }


    //注意参数的类型
    @Override
    public boolean replace(K key, final V oldValue, final V newValue) {
        Boolean result = doTask(key, new Task<Boolean>(TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY) {

            @Override
            protected Boolean execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry) {
                if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), oldValue)) {
                    entry.setValue(newValue);
                    return true;
                }
                return false;
            }
        });
        return (result == Boolean.TRUE);
    }


    @Override
    public V replace(K key, final V value) {
        return doTask(key, new Task<V>(TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY) {

            @Override
            @Nullable
            protected V execute(@Nullable Reference<K, V> ref, @Nullable Entry<K, V> entry) {
                if (entry != null) {
                    //获取旧的值
                    V oldValue = entry.getValue();
                    //设置新的值
                    entry.setValue(value);
                    return oldValue;
                }
                return null;
            }
        });
    }

    //清空segments
    @Override
    public void clear() {
        for (Segment s : this.segments) {
            s.clear();
        }
    }

    //purge all the entries on segments that no longer referenced

    /**
     * 他强制清洗不在被引用的entry，在频繁读取map而较少更新map的时候很有用
     */
    public void purgeUnreferenceEntries() {
        for (Segment s : this.segments) {
            s.restructureIfNecessary(false);
        }
    }


    @Override
    public int size() {
        int size = 0;
        for (Segment s : this.segments) {
            size += s.getCount();//引用的数量
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (Segment s : this.segments) {
            if (s.getCount() > 0) {
                return false;//只要有一个segment中含义一个引用，就表示不为空
            }
        }
        return true;
    }

    //--------ReferenceManager
    public class ReferenceManager {

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
    public interface Reference<K, V> {

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
    public static final class Entry<K, V> implements Map.Entry<K, V> {

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

    //初始化entrySet
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = this.entrySet;

        if (entrySet == null) {
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();//开始从第一个segment移动
        }

        @Override
        public boolean contains(@Nullable Object o) {
            //-1 判断类型
            if (o instanceof Map.Entry<?, ?>) {
                //-2 类型转换
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                Reference<K, V> ref = ConcurrentReferenceHashMap.this.getReference(entry.getKey(), Restructure.NEVER);

                Entry<K, V> otherEntry = ref != null ? ref.get() : null;
                if (otherEntry != null) {//此时otherEntry 和entry的key是一样的
                    //比较entry的value
                    return ObjectUtils.nullSafeEquals(otherEntry.getValue(), entry.getValue());
                }

            }
            return false;
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Map.Entry<?, ?>) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
            }
            return false;
        }

        @Override
        public int size() {
            return ConcurrentReferenceHashMap.this.size();//the num of segments
        }

        @Override
        public void clear() {
            ConcurrentReferenceHashMap.this.clear();
        }
    }


    private class EntryIterator implements Iterator<Map.Entry<K, V>> {

        private int segmentIndex;

        private int referenceIndex;

        @Nullable
        private Reference<K, V>[] references;
        @Nullable
        private Reference<K, V> reference;
        @Nullable
        private Entry<K, V> next;
        @Nullable
        private Entry<K, V> last;

        //构造方法：移动到下一个segment
        public EntryIterator() {
            moveToNextSegment();
        }

        @Override
        public boolean hasNext() {
            //获取下一个entry，索引判断的是下一个entry是否为null
            getNextIfNecessary();
            return (this.next != null);
        }

        @Override
        public Map.Entry<K, V> next() {
            getNextIfNecessary();
            if (this.next == null) {
                throw new NoSuchElementException();
            }

            this.last = this.next;
            this.next = null;
            return this.last;
        }

        private void getNextIfNecessary() {
            while (this.next == null) {
                moveToNextReference();
                if (this.reference == null) {
                    return;
                }
                this.next = this.reference.get();
            }
        }

        private void moveToNextReference() {
            if (this.reference != null) {
                this.reference = this.reference.getNext();
            }

            //当当前的ref为空并且当前的refIndex也超过了refs的长度，就要找下一个segment,并且把refIndex设为初始值0
            while (this.reference == null && this.references != null) {
                if (this.referenceIndex >= this.references.length) {
                    moveToNextSegment();
                    this.referenceIndex = 0;
                } else {
                    //
                    this.reference = this.references[referenceIndex];
                    this.referenceIndex++;
                }
            }
        }


        private void moveToNextSegment() {
            //先初始化segment里面的所有references和每个Ref
            this.reference = null;
            this.references = null;
            //segmentIndex初始值是0. note:here is if , not while
            if (this.segmentIndex < ConcurrentReferenceHashMap.this.segments.length) {
                //把每一个segment的refs付给references数组
                this.references = ConcurrentReferenceHashMap.this.segments[this.segmentIndex].references;
                this.segmentIndex++;
            }

        }

        @Override
        public void remove() {
            Assert.state(this.last != null, "no element to remove");
            ConcurrentReferenceHashMap.this.remove(this.last.getKey());
        }
    }
    /**
     * Various options
     */
    private enum TaskOption {

        RESTRUCTURE_BEFORE, RESTRUCTURE_AFTER, SKIP_IF_EMPTY, RESIZE
    }


    //枚举类
    public static enum Restructure {
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
