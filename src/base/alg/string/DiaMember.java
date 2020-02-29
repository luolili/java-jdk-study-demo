package base.alg.string;

public class DiaMember extends Member {
    // 折扣
    private double discount;

    public DiaMember(String id, String username, String password, double money, double discount) {
        super(id, username, password, money);
        this.discount = discount;
    }

    public void setDiscount(double discount) {
        // 修改钻石会员setDiscount方法，要求折扣范围在140%至160%之内
        if (discount < 1.4 || discount > 1.6) {
            return;
        }
        this.discount = discount;

    }

    public double getDiscount() {
        return discount;
    }
}
