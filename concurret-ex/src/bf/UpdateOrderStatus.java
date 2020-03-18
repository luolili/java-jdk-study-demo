package bf;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;

public class UpdateOrderStatus {

    private static int MAX_THREAD = 100;
    private static List<Order> orders = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        Long t = System.currentTimeMillis();

        final CyclicBarrier cb = new CyclicBarrier(MAX_THREAD + 1);
        for (int i = 0; i < MAX_THREAD; i++) {

            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            while (!orders.isEmpty()) {


                                Order order = null;
                                String token = UUID.randomUUID().toString();
                                int i = 0;
                                for (Order item : orders) {
                                    i++;
                                    if (item.getLock(token)) {
                                        order = item;
                                        break;
                                    }

                                }
                                // 获取到可处理的订单
                                if (order != null) {
                                    System.out.println(Thread.currentThread().getName());
                                    // 模拟订单处理功能
                                    try {
                                        Thread.sleep(100);
                                        //处理成功后移除
                                        orders.remove(order);
                                    } catch (Exception e) {

                                    } finally {
                                        //无论成功还是失败，解锁订单
                                        order.unlock(token);
                                    }
                                }

                            }
                            try {
                                cb.await();
                            } catch (Exception e) {

                            }
                        }

                    }
            ).start();

        }
    }
}
