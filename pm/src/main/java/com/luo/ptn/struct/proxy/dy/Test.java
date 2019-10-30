package com.luo.ptn.struct.proxy.dy;

import com.luo.ptn.struct.proxy.IOrderService;
import com.luo.ptn.struct.proxy.Order;
import com.luo.ptn.struct.proxy.OrderServiceImpl;

public class Test {

    public static void main(String[] args) {
        Order order = new Order();
        order.setUserId(1);
        IOrderService service = new OrderServiceImpl();
        IOrderService dy = (IOrderService) new OrderServiceDyProxy(service).bind();
        dy.save(order);

    }
}
