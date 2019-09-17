package base.set;

public class Confusing {

    public Confusing(Object o) {
        System.out.println("Object");
    }

    /* public Confusing(Object[] oArr) {
         System.out.println("Object arr");
     }*/
    public Confusing(double[] dArr) {
        System.out.println("dArr");
    }

  /*  public Confusing(int[] iArr) {
        System.out.println("iArr");
    }*/

    public static void main(String[] args) {
        new Confusing(null);//取相对具体的（子类的）一个
        final String s1 = "hu" + "fd";
        final String s2 = "hu" + new String("fd");
        System.out.println(s1 == s2);//false
    }


}
