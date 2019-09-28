package threadcoreknowledge.deadlock;

import java.util.Random;

/**
 * 多人随机转账
 */
public class MultiTransferMoney {

    private static final int NUM_ACCOUNT = 500;
    private static final int NUM_MONEY = 1000;
    private static final int NUM_THREAD = 20;
    private static final int NUM_ITER = 1000000;

    public static void main(String[] args) {
        Random rnd = new Random();
        TransferMoney.Account[] accounts = new TransferMoney.Account[NUM_ACCOUNT];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new TransferMoney.Account(NUM_MONEY);
        }

        class TransferThread extends Thread {
            @Override
            public void run() {
                for (int i = 0; i < NUM_ITER; i++) {
                    int fromAcc = rnd.nextInt(NUM_ACCOUNT);
                    int ToAcc = rnd.nextInt(NUM_ACCOUNT);
                    int amount = rnd.nextInt(NUM_MONEY);
                    TransferMoney.transferMoney(accounts[fromAcc], accounts[ToAcc], amount);
                }
            }
        }
        for (int i = 0; i < NUM_THREAD; i++) {
            new TransferThread().start();
        }
    }
}
