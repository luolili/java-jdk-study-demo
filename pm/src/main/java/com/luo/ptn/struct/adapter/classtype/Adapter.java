package com.luo.ptn.struct.adapter.classtype;

/**
 * 适配人是被适配人的子类，切实现目标接口，
 * 在接口方法里面调用 被适配人的方法，
 * 从而让适配人 通过 调用目标接口方法，来调用被适配人的方法
 */
public class Adapter extends Adaptee implements Target {

    @Override
    public void request() {
        super.adapteeRequest();
    }
}
