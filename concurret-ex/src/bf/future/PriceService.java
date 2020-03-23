package bf.future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PriceService {

    List<Shop> shops = Arrays.asList(new Shop("a1"),
            new Shop("a2"),
            new Shop("a3"),
            new Shop("a4")

    );

    public List<String> findPricesByDiscount(String product) {
        // 方法1：串行执行，4141
//        return shops.stream()
//                .map(shop->String.format("%s  price is %.2f",
//                        shop.getName(),shop.getPrice(product)))
//                .collect(Collectors.toList());
        // 方法2：并行流：1105，cpu有几个核就要几个线程，密集型计算：依赖cpu的运算能力
//        return shops.stream().parallel()
//                .map(shop->String.format("%s  price is %.2f",
//                        shop.getName(),shop.getPrice(product)))
//                .collect(Collectors.toList());

        // 方法3：CompletableFuture,2086，可调节线程数，远程调用：不依赖cpu的运算能力，需要更多的线程
        // 变为异步
        Executor exec = Executors.newFixedThreadPool(Math.min(shops.size() + 50, 100));
        // 指定线程个数，1079；如何计算个数：N*U*(1+W/C)
        int n = Runtime.getRuntime().availableProcessors();

        System.out.println("N=" + n);//4
        List<CompletableFuture<String>> futureList = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)), exec))
                .collect(Collectors.toList());
        // 顺序操作了，阻塞
        return futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public List<String> findPrices(String product) {


        // 方法3：CompletableFuture,2086，可调节线程数，远程调用：不依赖cpu的运算能力，需要更多的线程
        // 变为异步
        Executor exec = Executors.newFixedThreadPool(Math.min(shops.size() + 50, 100));
        List<CompletableFuture<String>> futureList = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), exec))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> DiscountService.applyDiscount(quote), exec)))
                .collect(Collectors.toList());
        // 顺序操作了，阻塞
        return futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public static void main(String[] args) {

        PriceService priceService = new PriceService();
        long st = System.currentTimeMillis();
        List<String> res = priceService.findPrices("ph");
        long diff = System.currentTimeMillis() - st;
        System.out.println("diff:" + diff);

    }
}
