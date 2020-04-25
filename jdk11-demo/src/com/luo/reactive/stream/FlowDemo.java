package com.luo.reactive.stream;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

public class FlowDemo {

    public static void main(String[] args) throws InterruptedException {
        //发布人
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        Subscriber<Integer> subscriber = new Subscriber<>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("accept:" + item);
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("end");
            }
        };

        publisher.subscribe(subscriber);

        for (int i = 0; i < 100; i++) {
            System.out.println("out:" + i);
            publisher.submit(i);

        }
        //正式env 在finally 执行他
        publisher.close();
        Thread.currentThread().join(100);
        System.out.println();

    }
}
