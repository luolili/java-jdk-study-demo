package base.alg.string;

public class GoldMember extends Member {
    // 充值赠送金额
    private double amount;

    public GoldMember(String id, String username, String password, double money, double amount) {
        super(id, username, password, money);
        this.amount = amount;
    }

    // 构造方法
    public GoldMember() {
    }

    public GoldMember(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
