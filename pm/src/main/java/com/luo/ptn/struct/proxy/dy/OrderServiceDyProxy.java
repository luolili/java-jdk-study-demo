package com.luo.ptn.struct.proxy.dy;

import com.luo.ptn.struct.proxy.Order;
import com.luo.ptn.struct.proxy.db.DataSourceContextHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class OrderServiceDyProxy implements InvocationHandler {

    private Object target;

    public OrderServiceDyProxy(Object target) {
        this.target = target;
    }

    public Object bind() {
        Class cls = target.getClass();
        return Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object argObject = args[0];
        beforeMethod(argObject);
        Object object = method.invoke(target, args);
        afterMethod();
        return object;
    }

    private void beforeMethod(Object obj) {

        int userId = 0;
        System.out.println("dy bef");
        if (obj instanceof Order) {
            Order order = (Order) obj;
            userId = order.getUserId();
            int dbRouter = userId % 2;
            System.out.println("--db" + dbRouter + "处理数据--");
            DataSourceContextHolder.setDBType("db" + String.valueOf(dbRouter));

        }
    }

    private void afterMethod() {
        System.out.println("aft");
    }
}
