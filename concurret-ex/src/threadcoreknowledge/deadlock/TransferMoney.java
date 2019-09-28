package threadcoreknowledge.deadlock;

public class TransferMoney implements Runnable {

    int flag = 1;
    static Account a = new Account(500);
    static Account b = new Account(500);

    public static void main(String[] args) throws InterruptedException {
        TransferMoney r1 = new TransferMoney();
        TransferMoney r2 = new TransferMoney();
        r1.flag = 1;
        r2.flag = 0;
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("a balance: " + a.balance);
        System.out.println("b balance: " + b.balance);


    }

    @Override
    public void run() {
        if (flag == 1) {
            transferMoney(a, b, 200);
        }

        if (flag == 0) {
            transferMoney(b, a, 200);
        }
    }

    public static void transferMoney(Account from, Account to, int amount) {
        synchronized (from) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                 
            }
            synchronized (to) {
                if (from.balance - amount < 0) {
                    System.out.println("余额不足");
                    return;
                }

                from.balance -= amount;
                to.balance += amount;
                System.out.println("转账成功：" + amount);

            }
        }
    }

    static class Account {
        int balance;//余额

        public Account(int balance) {
            this.balance = balance;
        }
    }
}
