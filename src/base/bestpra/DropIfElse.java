package base.bestpra;

/**
 * @author : luoli
 * @date : 2020-07-05 17:10
 * 去掉 if else
 */
public class DropIfElse {
    /**
     * 完全不必要的 else块
     *
     * @param num
     */
    public void f1(int num) {
        if (num > 3) {
            // do something
            return;
        } else {
            //do else
        }
    }

    /**
     * 好的做法
     *
     * @param num
     */
    public void rf1(int num) {
        if (num > 3) {
            // do something
            return;
        }
        //do else
    }

    /**
     * 价值匹配
     *
     * @param input0
     * @return
     */
    public String DetermineGender(int input0) {
        String gender = "";
        if (input0 == 0) {
            gender = "male";
        } else if (input0 == 1) {
            gender = "woman";
        } else {
            gender = "unknown";
        }
        return gender;
    }

    /**
     * 好的做法1
     * 用字典
     *
     * @param input0
     * @return
     */
    public String rDetermineGender(int input0) {
        if (input0 == 0) {
            return "male";
        }
        if (input0 == 1) {
            return "woman";
        }
        return "unknown";
    }


}
