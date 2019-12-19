package base.bestpra;

/**
 * NullPointerException
 * 减少 x!=null检查
 */
public class NPE {

    public static void main(String[] args) {
        //1.在 已知的字符串而不是未知对象上调用Equals
        Object unknownObj = null;
        if ("xc".equals(unknownObj)) {
            System.err.println("err");
        }

        //2.优先用 valueOf 而不是 toString
        System.out.println(String.valueOf(unknownObj));

        //3.用 安全的库：apache common
        //4.避免从方法里面返回 null,而是要返回空集合/数组
        //5.空对象模式
        //6.在数据库里面约束
        //7.
    }
}
