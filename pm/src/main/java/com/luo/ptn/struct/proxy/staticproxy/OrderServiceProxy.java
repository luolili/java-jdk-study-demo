package com.luo.ptn.struct.proxy.staticproxy;

import com.luo.ptn.struct.proxy.IOrderService;
import com.luo.ptn.struct.proxy.Order;
import com.luo.ptn.struct.proxy.OrderServiceImpl;
import com.luo.ptn.struct.proxy.db.DataSourceContextHolder;

public class OrderServiceProxy {
    private IOrderService orderService;//target,增强里面的save方法

    public int save(Order order) {
        beforeMethod();
        orderService = new OrderServiceImpl();

        Integer userId = order.getUserId();
        int dbRouter = userId % 2;
        System.out.println("--db" + dbRouter + "处理数据--");

        DataSourceContextHolder.setDBType("db" + String.valueOf(dbRouter));

        afterMethod();
        return orderService.save(order);
    }

    private void beforeMethod() {
        System.out.println("bef");
    }

    private void afterMethod() {
        System.out.println("aft");
    }


}
