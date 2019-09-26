# rmq
1. rmq有 哪几种广播类型
- fanout : 所有 绑定到 此 exchange 的 queue 都可以接受消息
- direct: 通过 routingKey 和 exchange 决定 唯一的 queue 可以接受消息
- topic: 所有符合 routingKey的 都可以接受消息
