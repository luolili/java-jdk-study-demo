package com.luo.ptn.behavior.strategy;

public class Test {
    public static void main(String[] args) {
        PromotionActivity a1 =
                new PromotionActivity(new LijianPromotionStrategy());
        PromotionActivity a2 =
                new PromotionActivity(new ManjianPromotionStrategy());

        a1.executePromotionStrategy();
        a2.executePromotionStrategy();

        String promotionKey = "";
        PromotionActivity a3 = new PromotionActivity(PromotionStrategyFactory.getStrategy(promotionKey));
        a3.executePromotionStrategy();


    }
}
