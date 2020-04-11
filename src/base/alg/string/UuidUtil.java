package base.alg.string;

import java.util.UUID;

public class UuidUtil {

    public static String genUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static void main(String[] args) {
        String res = genUUID();
        System.out.println(res);
    }
}
