package com.luo.ptn.struct.proxy.staticproxy;

import com.luo.ptn.struct.proxy.Order;

public class Test {

    public static void main(String[] args) {
        Order order = new Order();
        order.setUserId(1);
        OrderServiceProxy orderServiceProxy = new OrderServiceProxy();
        orderServiceProxy.save(order);

    }
}
