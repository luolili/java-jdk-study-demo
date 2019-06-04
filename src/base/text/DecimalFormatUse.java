package base.text;

import java.text.DecimalFormat;

/**
 * num to String
 */
public class DecimalFormatUse {
    public static void main(String[] args) {
        // for double
        double a = 3.232456;
        DecimalFormat decimalFormat01 = new DecimalFormat("0");
        System.out.println(decimalFormat01.format(a));//3

        //3.232
        DecimalFormat decimalFormat02 = new DecimalFormat("0.000");

        String s = decimalFormat02.format(a);
        System.out.println(decimalFormat02.format(a));

        DecimalFormat decimalFormat03 = new DecimalFormat("#");
        System.out.println(decimalFormat03.format(a));//3

        int b = 2;
        DecimalFormat intFormat01 = new DecimalFormat("000");
        System.out.println(intFormat01.format(b));//002


    }
}
