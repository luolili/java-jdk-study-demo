package com.luo.reactive.stream;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

public class MyProcessor extends SubmissionPublisher<String>
        implements Processor<Integer, String> {
    private Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        System.out.println("处理器接受到数据: " + item);
        // 过滤掉小于0的, 然后发布出去
        if (item > 0) {
            this.submit("转换后的数据:" + item);
        }
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        // 出现了异常(例如处理数据的时候产生了异常)
        throwable.printStackTrace();

        // 我们可以告诉发布者, 后面不接受数据了
        this.subscription.cancel();
    }

    @Override
    public void onComplete() {
        System.out.println("处理器处理完了!");
        // 关闭发布者
        this.close();
    }
}
