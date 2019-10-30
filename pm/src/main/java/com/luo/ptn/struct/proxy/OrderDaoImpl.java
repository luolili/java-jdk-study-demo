package com.luo.ptn.struct.proxy;

public class OrderDaoImpl implements IOrderDao {
    @Override
    public int insert(Order order) {
        System.out.println("insert su");
        return 1;
    }
}
