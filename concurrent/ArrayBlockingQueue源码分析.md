##了解 Queue 
Queue 是一个继承 Collection的接口。

主要方法：

```
// 若加入成功，返回 true ，否则 因为没有更多的容量而抛出IllegalStateException
boolean add(E e)
```

```
// 和add 方法类似，适合于 有容量限制的队列
boolean offer(E e)

```
```
// 删除队列 的 头，若队列为空，抛出NoSuchElementException
E remove();
//若队列为空，不会抛出NoSuchElementException
E poll();
```
```
// 获取队列 的 头，若队列为空，抛出NoSuchElementException
E element();
//若队列为空，不会抛出NoSuchElementException
E peek();
```
##了解 BlockingQueue
他是一个接口，继承 Queue.

增加的方法：
```
// 把 队列的可用的元素 运输（流出）到  Collection 类型的集合里面，若Collection 类型是 队列本身，会抛出IllegalArgumentException；参数为 null，会有NPE
int drainTo(Collection<? super E> c);
//指定 要运输的元素最大个数
int drainTo(Collection<? super E> c, int maxElements);
```

##认识 AbstractQueue
他 实现了 Queue 接口。

方法实现：
```
// add 方法 依赖 offer 方法的实现, 该抽象类没有实现 offer 方法，由他的子类来实现
 public boolean add(E e) {
        if (offer(e))
            return true;
        else
            throw new IllegalStateException("Queue full");
    }
    // 同上 poll方法也是由子类实现
  public E remove() {
         E x = poll();
         if (x != null)
             return x;
         else
             throw new NoSuchElementException();
     }   
     
 public E element() {
         E x = peek();
         if (x != null)
             return x;
         else
             throw new NoSuchElementException();
     }
   
   public void clear() {
           while (poll() != null)
               ;
       }
  //把集合 转为 队列
    public boolean addAll(Collection<? extends E> c) {
          if (c == null)
              throw new NullPointerException();
          if (c == this)
              throw new IllegalArgumentException();
          boolean modified = false;
          for (E e : c)
              if (add(e))
                  modified = true;
          return modified;
      }
```
##ArrayBlockingQueue
他是一个 经典的 有边界的缓存，具有固定的大小的数组，由生产者放入元素，消费者取出元素。一旦他被创建，他的容量就不可改变。从后面加入，从前面取出。


他是基于 数组（Object[]），有 takeIndex,putIndex，count,lock等属性。

方法：
```
//用 ReentrantLock 来做线程安全,依赖 enqueue 方法来加入元素
public boolean offer(E e) {
        // 不允许 null 元素
        checkNotNull(e);
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if (count == items.length)
                return false;
            else {
                enqueue(e);
                return true;
            }
        } finally {
            lock.unlock();
        }
    }
```
```
 private void enqueue(E x) {
        // assert lock.getHoldCount() == 1;
        // assert items[putIndex] == null;
        //把原 数组的 引用 给 新的数组，并且 该引用不变，从而用新数组改变 items 的内容
        final Object[] items = this.items;
        items[putIndex] = x;
        // 表示 队列已满
        if (++putIndex == items.length)
            putIndex = 0;
        count++;
        notEmpty.signal();
    }
```
删除：
```
 public E poll() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return (count == 0) ? null : dequeue();
        } finally {
            lock.unlock();
        }
    }

private E dequeue() {
        // assert lock.getHoldCount() == 1;
        // assert items[takeIndex] != null;
        final Object[] items = this.items;
        @SuppressWarnings("unchecked")
        // takeIndex 默认是0，先进先出
        E x = (E) items[takeIndex];
        //不允许加 null,可以设置元素 为null
        items[takeIndex] = null;
        if (++takeIndex == items.length)
            takeIndex = 0;
        count--;
        if (itrs != null)
            itrs.elementDequeued();
        notFull.signal();
        return x;
    }

  public void put(E e) throws InterruptedException {
        checkNotNull(e);
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == items.length)
            //空间不够的时候要等
                notFull.await();
            enqueue(e);
        } finally {
            lock.unlock();
        }
    }
```
