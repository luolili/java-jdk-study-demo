# rmq
1. rmq有 哪几种广播类型
- fanout : 所有 绑定到 此 exchange 的 queue 都可以接受消息
- direct: 通过 routingKey 和 exchange 决定 唯一的 queue 可以接受消息
- topic: 所有符合 routingKey的 都可以接受消息

2.rmq 如何保证消息的稳定性？
> 消费者 在消费完消息之后， 会发送一个回执 给mq, mq收到回执后才把消息从 queue 里面移除，
如果mq 没有收到回执，并发现 消费者 的mq 链接断开了，mq会把消息发给其他消费者。
