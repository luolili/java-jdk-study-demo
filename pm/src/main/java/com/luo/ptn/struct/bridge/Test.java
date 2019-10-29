package com.luo.ptn.struct.bridge;

public class Test {
    public static void main(String[] args) {
        // 排列 组合
        Bank ibank = new ICACBank(new DepositAccount());
        Account iaccount = ibank.openAccount();
        iaccount.showAccount();

        Bank banki2 = new ICACBank(new SavingAccount());
        Account iaccount2 = banki2.openAccount();
        iaccount2.showAccount();

        Bank abank = new ABCBank(new DepositAccount());
        Account aaccount = abank.openAccount();
        aaccount.showAccount();

        Bank abank2 = new ABCBank(new SavingAccount());
        Account aaccount2 = abank2.openAccount();
        aaccount2.showAccount();


    }
}
