package com.luo.ptn.struct.facade;

public class Test {
    public static void main(String[] args) {
        PointsGift pointsGift = new PointsGift();
        GiftExchangeService giftExchangeService = new GiftExchangeService();
        QualifyService qualifyService = new QualifyService();
        PointsPaymentService pointsPaymentService = new PointsPaymentService();
        ShippingService shippingService = new ShippingService();
        giftExchangeService.setPointsPaymentService(pointsPaymentService);
        giftExchangeService.setQualifyService(qualifyService);
        giftExchangeService.setShippingService(shippingService);

        giftExchangeService.giftExchange(pointsGift);


    }
}
