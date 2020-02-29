package base.alg.string;


public class Member {
    // id
    private String id;
    // 姓名
    private String username;
    // 密码
    private String password;
    // 余额
    private double money;

    public Member() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {

        this.id = id;

    }

    public String getPassword() {
        // 修改getPassword 方法，要求每次都返回null 值
        return null;

    }

    public void setPassword(String password) {
        // 密码方法新密码长度在6位至9位，否则不能修改；
        if (this.id.length() < 6 || this.id.length() > 9) {
            System.out.println("密码方法新密码长度在6位至9位");
            return;
        }
        this.password = password;

    }

    public double getMoney() {

        return money;

    }

    public void setMoney(double money) {

        this.money = money;

    }

    public Member(String id, String username, String password, double money) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.money = money;
    }
}
