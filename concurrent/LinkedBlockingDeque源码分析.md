##了解 Deque 
Deque 是一个继承 Queue 的接口。

增加的方法：

```
// 若加入成功，返回 true ，否则 因为没有更多的容量而抛出IllegalStateException
boolean addFirst(E e)
boolean addLast(E e)
```

```
// 和add 方法类似，适合于 有容量限制的队列
boolean offerFirst(E e)
boolean offerLast(E e)

```
```
// 删除队列 的 头，若队列为空，抛出NoSuchElementException
E removeFirst();
//若队列为空，不会抛出NoSuchElementException
E pollFirst();
```
```
// 获取队列 的 头，若队列为空，抛出NoSuchElementException
E element();
//若队列为空，不会抛出NoSuchElementException
E peekFirst();
```
##了解 BlockingDeque
他是一个接口，继承 BlockingQueue.
 
##LinkedBlockingDeque
他是一个 经典的 有边界的缓存，具有固定的大小的数组，由生产者放入元素，消费者取出元素。一旦他被创建，他的容量就不可改变。从后面加入，从前面取出。


他是基于 数组（Object[]），有 Node 类型的 first,last,capacity，count,lock等属性。

方法：
```
//add 方法等价于 addLast 方法，相当于 Queue 的 add 方法
  public boolean add(E e) {
        addLast(e);
        return true;
    }

  public void addLast(E e) {
        if (!offerLast(e))
            throw new IllegalStateException("Deque full");
    }
    //基于 Node
  public boolean offerLast(E e) {
        if (e == null) throw new NullPointerException();
        Node<E> node = new Node<E>(e);
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return linkLast(node);
        } finally {
            lock.unlock();
        }
    }
```
```
  private boolean linkLast(Node<E> node) {
          // assert lock.isHeldByCurrentThread();
          if (count >= capacity)
              return false;
           // 获得加入前的最后一个 元素
          Node<E> l = last;
          // 加入node之后，l 变为 node.prev
          node.prev = l;
          //加入后 最后一个元素是 node
          last = node;
          if (first == null)
              first = node;
          else
              l.next = node;
          ++count;
          notEmpty.signal();
          return true;
      }
```
addFirst：
```
//类似的模式，依赖于 offerFirst 方法
    public void addFirst(E e) {
          if (!offerFirst(e))
              throw new IllegalStateException("Deque full");
      }
```
```.
 public boolean offerFirst(E e) {
        if (e == null) throw new NullPointerException();
        Node<E> node = new Node<E>(e);
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return linkFirst(node);
        } finally {
            lock.unlock();
        }
    }

```
```
 private boolean linkFirst(Node<E> node) {
        // assert lock.isHeldByCurrentThread();
        if (count >= capacity)
            return false;
        Node<E> f = first;
        node.next = f;
        first = node;
        if (last == null)
            last = node;
        else
            f.prev = node;
        ++count;
        notEmpty.signal();
        return true;
    }

```
