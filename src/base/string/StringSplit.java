package base.string;

/**
 * @author : luoli
 * @date : 2020-05-27 15:45
 */
public class StringSplit {

    public static void main(String[] args) {
        String str = "a,b,c,,,";
        String[] res = str.split(",");
        System.out.println(res.length);
    }
}
