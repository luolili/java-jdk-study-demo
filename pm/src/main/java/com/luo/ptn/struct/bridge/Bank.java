package com.luo.ptn.struct.bridge;

/**
 * 账号 和 bank 独立的变化
 */
public abstract class Bank {
    //用组合 桥接
    protected Account account;//只有子类可以拿到他

    public Bank(Account account) {
        this.account = account;
    }

    abstract Account openAccount();//和接口的 method name 一样
}
