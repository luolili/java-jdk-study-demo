package com.luo.ptn.struct.facade;

public class PointsPaymentService {

    public boolean pay(PointsGift pointsGift) {

        System.out.println("pay " + pointsGift.getName());
        return true;
    }
}
