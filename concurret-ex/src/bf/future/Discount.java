package bf.future;

public enum Discount {
    // 必须放在前面
    NONE(0),
    SILVER(5),
    GOLD(10),
    PLATINUM(15),
    DIAMOND(20);
    int percent;

    Discount(int percent) {
        this.percent = percent;
    }

}
