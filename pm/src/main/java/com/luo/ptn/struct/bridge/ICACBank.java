package com.luo.ptn.struct.bridge;

public class ICACBank extends Bank {

    public ICACBank(Account account) {
        super(account);
    }

    @Override
    Account openAccount() {
        System.out.println("open icac acc");
        account.openAccount();//委托给 account 实现 openAccount
        return account;
    }
}
