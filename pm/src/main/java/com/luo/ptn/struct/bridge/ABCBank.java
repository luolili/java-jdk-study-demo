package com.luo.ptn.struct.bridge;

public class ABCBank extends Bank {

    public ABCBank(Account account) {
        super(account);
    }

    @Override
    Account openAccount() {
        System.out.println("open alg acc");
        account.openAccount();//委托给 account 实现 openAccount
        return account;
    }
}
