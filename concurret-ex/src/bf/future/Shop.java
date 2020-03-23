package bf.future;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Shop {

    private String name;

    public Shop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    static Random r = new Random();

    public static void delay() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
    }

    public double getPrice(String product) {
        delay();
        return r.nextDouble() * 100;
    }

    public String getPriceByDiscount(String product) {
        delay();
        double price = r.nextDouble();
        Discount d = Discount.values()[r.nextInt(Discount.values().length)];
        return String.format("%s: price:%.2f:discount:%s", getName(), price, d);
    }

    public Future<Double> getPriceAsyn(String product) {
        //方法1
//        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
//        new Thread(()->futurePrice.complete(getPrice(product))).start();
        // return futurePrice;
        // 方法2（推荐）:异步调用 getPrice 方法
        return CompletableFuture.supplyAsync(() -> getPrice(product));
    }

    public static void main(String[] args) throws Exception {
        Shop shop = new Shop("phone");
        long start = System.currentTimeMillis();
        Future<Double> future = shop.getPriceAsyn(shop.getName());
        long diff = System.currentTimeMillis() - start;
        System.out.println("diff:" + diff);
        long st1 = System.currentTimeMillis();
        Double res = future.get();
        long diff1 = System.currentTimeMillis() - st1;
        System.out.println("diff1:" + diff1);
        System.out.println("res:" + res);


    }
}
