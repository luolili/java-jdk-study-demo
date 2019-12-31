package threadcoreknowledge.escapeanalysis;

public class Test {

    /**
     * bytecode 没有monitorenter/exit
     */
    public void f() {
        Object o = new Object();
        synchronized (o) {
            System.out.println("objb:" + o);
        }
    }

    //jit 编译后代码
    public void res() {
        Object o = new Object();
        System.out.println("objb:" + o);
    }

    public String concatStr(String s1, String s2) {
        StringBuffer sb = new StringBuffer();

        sb.append(s1);
        sb.append(s2);
        return sb.toString();
    }

    public static void main(String[] args) {

    }
}
