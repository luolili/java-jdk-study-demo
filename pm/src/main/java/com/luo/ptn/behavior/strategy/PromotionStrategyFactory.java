package com.luo.ptn.behavior.strategy;

import java.util.HashMap;
import java.util.Map;

public class PromotionStrategyFactory {
    private static Map<String, PromotionStrategy> map = new HashMap<>();

    static {
        map.put(PromotionKey.LI_JIAN, new LijianPromotionStrategy());
        map.put(PromotionKey.MAN_JIAN, new ManjianPromotionStrategy());
        map.put(PromotionKey.FAN_XIAN, new FanxianPromotionStrategy());
    }

    private PromotionStrategyFactory() {

    }

    public static PromotionStrategy getStrategy(String key) {
        return map.get(key);
    }

    private interface PromotionKey {
        String LI_JIAN = "li jian";
        String MAN_JIAN = "man jian";
        String FAN_XIAN = "fan xian";
    }
}
