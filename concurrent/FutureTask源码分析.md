##了解 Future 
他是一个接口，表示 一个异步运算的结果，提供的方法是为了检查任务是否完成，若没有，就等他完成。可获得计算的结果。通过 get 方法获得结果，若任务没有完成，会 block.

```
//参数为 true 表示正在执行任务的线程可能被打断，返回 false 表示任务已经完成，无法取消
 boolean cancel(boolean mayInterruptIfRunning);
    //取消成功 返回 true
  boolean isDone();
  //任务 完成 返回true
  boolean isDone();
```
```
V get() throws InterruptedException, ExecutionException;
//可设置等待的时间
V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
```
##FutureTask
他实现了 RunnableFuture:是 Runnable 和 Future 2个接口的 合体。

![Image text](../concurrent/pic/FutureTask.png)

主要的属性：  
outcome:计算的结果；callable（被执行的东西）, Thread runner. 
```
 private volatile int state;
    //初始值
    private static final int NEW          = 0;
    //任务完成，但结果还未保存 到 outcome
    private static final int COMPLETING   = 1;
    // 任务完成，结果保存 到 outcome
    private static final int NORMAL       = 2;
    //发生异常，异常保存 到 outcome
    private static final int EXCEPTIONAL  = 3;
    private static final int CANCELLED    = 4;
    private static final int INTERRUPTING = 5;
    private static final int INTERRUPTED  = 6;
``` 
主要方法：  
```
 private V report(int s) throws ExecutionException {
        Object x = outcome;
        if (s == NORMAL)
            return (V)x;
        if (s >= CANCELLED)
            throw new CancellationException();
        throw new ExecutionException((Throwable)x);
    }
```
```
 public FutureTask(Callable<V> callable) {
        if (callable == null)
            throw new NullPointerException();
        this.callable = callable;
        //任务运行的初始化状态：NEW;completing:1;normal:2
        this.state = NEW;       // ensure visibility of callable
    }
```

```
  public FutureTask(Runnable runnable, V result) {
        //适配器模式Rannable-->Callable
        this.callable = Executors.callable(runnable, result);
        this.state = NEW;       // ensure visibility of callable
    }
```
```
 public void run() {
        //state 不是初始,替换runner为当前线程
        if (state != NEW ||
            !UNSAFE.compareAndSwapObject(this, runnerOffset,
                                         null, Thread.currentThread()))
            return;
        try {
            Callable<V> c = callable;
            if (c != null && state == NEW) {
                V result;
                //是否运行的标签
                boolean ran;
                try {
                    result = c.call();
                    ran = true;
                } catch (Throwable ex) {
                    result = null;
                    ran = false;
                    setException(ex);
                }
                if (ran)
                    //把结果放入到 outcome
                    set(result);
            }
        } finally {
            // runner must be non-null until state is settled to
            // prevent concurrent calls to run()
            runner = null;
            // state must be re-read after nulling runner to prevent
            // leaked interrupts
            int s = state;
            if (s >= INTERRUPTING)
                handlePossibleCancellationInterrupt(s);
        }
    }
```