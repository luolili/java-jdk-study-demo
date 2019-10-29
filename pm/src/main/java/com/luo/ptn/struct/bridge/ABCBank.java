package com.luo.ptn.struct.bridge;

public class ABCBank extends Bank {

    public ABCBank(Account account) {
        super(account);
    }

    @Override
    Account openAccount() {
        System.out.println("open alg acc");
        account.openAccount();
        return account;
    }
}
