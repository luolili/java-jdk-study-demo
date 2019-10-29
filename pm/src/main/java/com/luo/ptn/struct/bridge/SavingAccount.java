package com.luo.ptn.struct.bridge;

/**
 * 定期 账号
 */
public class SavingAccount implements Account {
    @Override
    public Account openAccount() {
        System.out.println("open saving account");
        return new SavingAccount();
    }

    @Override
    public void showAccount() {
        System.out.println("this is saving account");
    }
}
