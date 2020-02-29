package base.alg.string;

import java.util.Scanner;

public class Front {
    /**
     * 如果type为0则创建一个Member普通会员，如果type为1则创建一个黄金会员，如果type为2则创建一个钻石会员。返回值为办卡时创建的Member会员对象
     */
    public Member openMember(String id, String username,
                             String password, int type) {
        if (type == 0) {
            return new Member(id, username, password, 0);
        } else if (type == 1) {
            return new GoldMember(id, username, password, 0, 0);
        } else if (type == 2) {
            return new DiaMember(id, username, password, 0, 0);
        }
        return null;
    }

    /**
     * 如果会员卡为黄金会员，会奖励充值金额，并显示出赠送金额；如果会员卡为钻石会员，则需要计算出赠送的金额并显示出来。返回值表示充值后的余额
     */
    public double recharge(Member member, double money) {
        if (member instanceof GoldMember) {
            GoldMember gm = (GoldMember) member;
            gm.setMoney(gm.getMoney() + money);
            gm.setAmount(money);
            System.out.println("奖励的充值金额:" + gm.getAmount());
            System.out.println("赠送金额:" + gm.getAmount());
            return gm.getMoney();
        }
        if (member instanceof DiaMember) {
            DiaMember dm = (DiaMember) member;
            double discount = dm.getDiscount();
            double amount = discount * money;
            System.out.println("奖励的充值金额:" + (amount - money));
            System.out.println("赠送金额:" + (amount - money));
            dm.setMoney(dm.getMoney() + amount);
            return dm.getMoney();
        }
        member.setMoney(member.getMoney() + money);
        return member.getMoney();
    }

    /**
     * 数，member表示为充值会员卡，hour表示为游戏时长，cost表示为每小时费用。如果会员卡为黄金会员将享受8折优惠，并显示出优惠金额；如果会员卡为钻石会员将享受7折优惠，并显示出优惠金额。返回值表示计费后的余额。
     */
    double pay(Member member, int hour, double cost) {
        if (member instanceof GoldMember) {
            GoldMember gm = (GoldMember) member;
            gm.setMoney(gm.getMoney() - hour * cost * 0.8);
            System.out.println("优惠后支付的金额：" + hour * cost * 0.8);
            System.out.println("优惠金额：" + hour * cost * 0.2);
            return gm.getMoney();
        }
        if (member instanceof DiaMember) {
            DiaMember dm = (DiaMember) member;
            dm.setMoney(dm.getMoney() - hour * cost * 0.7);
            System.out.println("钻石会员优惠金额：" + hour * cost * 0.3);
            System.out.println("钻石会员优惠后需要支付的金额：" + hour * cost * 0.7);
            return dm.getMoney();
        }
        return member.getMoney() - hour * cost;
    }

    public static void main(String[] args) {
        Front front = new Front();
        Scanner input = new Scanner(System.in);
        System.out.println("请输入id：");
        String id = input.next();
        System.out.println("请输入名字：");
        String username = input.next();
        System.out.println("请输入密码：");
        String password = input.next();
        System.out.println("请输入类型：");
        String type = input.next();
        int t = 0;
        if ("钻石".equals(type)) {
            t = 2;
        }
        if ("黄金".equals(type)) {
            t = 1;
        }
        if ("普通".equals(type)) {
            t = 0;
        }
        Member gm = front.openMember(id, username, password, t);
        gm.setPassword(password);
        GoldMember m1 = (GoldMember) gm;
        gm.setMoney(100);

        System.out.println("请输入得到的赠送金额：");
        String money = input.next();
        double mon1 = front.recharge(m1, Double.parseDouble(money));

        System.out.println("请输入游戏时长：");
        String hour = input.next();
        System.out.println("请输入每小时费用：");
        String cost = input.next();

        double mon3 = front.pay(m1, Integer.parseInt(hour), Integer.parseInt(cost));
        System.out.println("剩余金额：" + mon3);
    }
}
