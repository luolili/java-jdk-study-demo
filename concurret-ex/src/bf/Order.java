package bf;

public class Order {
    private boolean lock = true;
    private String token = "";

    public boolean getLock(String token) {
        if (lock) {
            synchronized (this) {
                if (lock) {
                    this.lock = false;
                    this.token = token;
                    return true;
                } else {
                    System.out.println("被别人抢走了");
                }
            }
        }
        return lock;
    }

    public void unlock(String newToken) {

        if (!token.equals(newToken) || lock) {
            System.out.println("error");
        } else {
            lock = true;
        }
    }
}
