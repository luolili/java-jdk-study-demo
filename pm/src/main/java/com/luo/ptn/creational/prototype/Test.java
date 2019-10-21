package com.luo.ptn.creational.prototype;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws CloneNotSupportedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

      /*  Mail mail = new Mail();
        mail.setContent("初始化模板");

        for (int i = 0; i < 10; i++) {
            Mail mailTemp = (Mail) mail.clone();
            mailTemp.setName("name:" + i);
            mailTemp.setMailAddress("name" + i + "@ee");
            mailTemp.setContent("oo");
            MailUtil.sendMail(mail);

        }
        MailUtil.saveOriginMailRecord(mail);
*/
        HungrySingleton01 instance = HungrySingleton01.getInstance();
        Method method = instance.getClass().getDeclaredMethod("clone");
        //method.setAccessible(true);
        HungrySingleton01 copy = (HungrySingleton01) method.invoke(instance);
        //破坏 单利
        System.out.println(instance);//.HungrySingleton01@12b1dae
        System.out.println(copy);//.HungrySingleton01@1530d0a
        Map map = new HashMap<>();




    }
}
