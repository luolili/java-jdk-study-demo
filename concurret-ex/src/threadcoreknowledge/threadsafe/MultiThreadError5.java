package threadcoreknowledge.threadsafe;


/**
 * 发布: 监听器; 观察者模式
 */
public class MultiThreadError5 {

    int count;

    public MultiThreadError5(MySource mySource) {
        mySource.registerListener(new EventListener() {
            @Override
            public void onEvent(Event e) {
                System.out.println("count: " + count);
            }
        });

        for (int i = 0; i < 10000; i++) {
            System.out.print(i);
        }
        count = 100;
    }

    public static void main(String[] args) {
        MySource mySource = new MySource();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
                mySource.eventCome(new Event() {
                });
            }
        }).start();

        MultiThreadError5 multiThreadError5 = new MultiThreadError5(mySource);


    }

    static class MySource {
        private EventListener eventListener;

        void registerListener(EventListener eventListener) {
            this.eventListener = eventListener;
        }

        void eventCome(Event e) {
            if (eventListener != null) {
                eventListener.onEvent(e);
            } else {
                System.out.println("no");
            }
        }
    }

    interface EventListener {
        void onEvent(Event e);
    }

    interface Event {

    }
}