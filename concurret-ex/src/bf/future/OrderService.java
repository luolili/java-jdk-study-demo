package bf.future;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 订单的统计
 */
public class OrderService {

    public String getTodayOrderCount() {
        System.out.println("统计今日的订单数量" + Thread.currentThread().getName());
        try {
            Thread.sleep(100);
        } catch (Exception e) {

        }
        return "20";
    }

    public String getTodayTurnover() {
        System.out.println("统计今日的交易额" + Thread.currentThread().getName());
        try {
            Thread.sleep(200);
        } catch (Exception e) {

        }
        return "200";
    }

    public String getTotalTurnover() {
        System.out.println("统计总交易额" + Thread.currentThread().getName());
        try {
            Thread.sleep(300);
        } catch (Exception e) {

        }
        return "2000";
    }

    public CompletableFuture<String> todayOrderCount() {
        return CompletableFuture.supplyAsync(() -> this.getTodayOrderCount());
    }

    public CompletableFuture<String> todayTurnover() {
        return CompletableFuture.supplyAsync(() -> this.getTodayTurnover());
    }

    public CompletableFuture<String> totalTurnover() {
        return CompletableFuture.supplyAsync(() -> this.getTotalTurnover());
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        OrderService orderService = new OrderService();
        long st = System.currentTimeMillis();
        CompletableFuture<String> todayOrderCountFuture = orderService.todayOrderCount();
        CompletableFuture<String> todayTurnoverFuture = orderService.todayTurnover();
        CompletableFuture<String> totalTurnoverFuture = orderService.totalTurnover();

        todayOrderCountFuture.whenComplete((v, t) -> {
            map.put("todayOrderCount", v);
        });
        todayTurnoverFuture.whenComplete((v, t) -> {
            map.put("todayTurnover", v);
        });
        totalTurnoverFuture.whenComplete((v, t) -> {
            map.put("totalTurnover", v);
        });
        CompletableFuture.allOf(todayOrderCountFuture, todayTurnoverFuture, totalTurnoverFuture)
                .thenRun(() -> System.out.println("完成"))
                .join();
        long diff = System.currentTimeMillis() - st;
        System.out.println("diff:" + diff);
        System.out.println(map);


    }

}
