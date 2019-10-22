package com.luo.ptn.struct.facade;

import lombok.Data;

/**
 * 外观类
 */
@Data
public class GiftExchangeService {

    private PointsPaymentService pointsPaymentService;
    private QualifyService qualifyService;
    private ShippingService shippingService;

    public void giftExchange(PointsGift pointsGift) {
        if (qualifyService.isAvailable(pointsGift)) {
            if (pointsPaymentService.pay(pointsGift)) {
                String orderNo = shippingService.shipGift(pointsGift);
                System.out.println("su " + orderNo);
            }
        }
    }
}
