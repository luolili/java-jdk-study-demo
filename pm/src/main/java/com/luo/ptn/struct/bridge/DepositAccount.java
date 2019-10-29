package com.luo.ptn.struct.bridge;

/**
 * 定期 账号
 */
public class DepositAccount implements Account {
    @Override
    public Account openAccount() {
        System.out.println("open deposit account");
        return new DepositAccount();
    }

    @Override
    public void showAccount() {
        System.out.println("this is deposit acc");
    }
}
