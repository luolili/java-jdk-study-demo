package base.alg;

/**
 * 自动售货机里有 N 瓶复制可乐。复制可乐非常神奇，喝了它的人会复制出一个自己来！ 现在有 Alice, Bob, Cathy, Dave 四个人在排队买复制可乐。买完的人会马上喝掉，然后他和他的副本会重新去队伍的最后面排队买可乐。 问最后一个买到复制可乐的人叫什么名字？
 * input:输入仅有一行，包含一个正整数 N (1 <= N <= 1,000,000,000)，表示可乐的数量。
 * <p>
 * output:输出喝到最后一罐复制可乐的人的名字。
 */
public class WhoDrinksLast {

    public static void main(String[] args) {
        String s = drinkLast(8);
    }

    public static String drinkLast(int n) {

        String[] names = {"A", "B", "C", "D"};
        int base = names.length;
        // 表示有几个自己
        int i = 0;
        while (n > base) {
            n -= base;
            // copy itself
            base *= 2;
            i++;

        }
        int index = (int) Math.ceil(n / Math.pow(2, i));
        return names[index - 1];
    }
}
