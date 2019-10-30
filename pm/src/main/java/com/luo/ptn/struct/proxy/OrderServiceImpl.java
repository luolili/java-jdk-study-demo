package com.luo.ptn.struct.proxy;

public class OrderServiceImpl implements IOrderService {

    private IOrderDao orderDao;

    @Override
    public int save(Order order) {
        orderDao = new OrderDaoImpl();
        System.out.println("service su");

        return orderDao.insert(order);
    }
}
