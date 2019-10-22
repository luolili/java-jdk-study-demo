package com.luo.ptn.struct.facade;

public class QualifyService {

    public boolean isAvailable(PointsGift pointsGift) {
        System.out.println("verify " + pointsGift.getName() + " pass");
        return true;
    }
}
