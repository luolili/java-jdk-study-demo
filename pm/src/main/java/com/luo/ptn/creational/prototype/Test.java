package com.luo.ptn.creational.prototype;

public class Test {
    public static void main(String[] args) throws CloneNotSupportedException {

        Mail mail = new Mail();
        mail.setContent("初始化模板");

        for (int i = 0; i < 10; i++) {
            Mail mailTemp = (Mail) mail.clone();
            mailTemp.setName("name:" + i);
            mailTemp.setMailAddress("name" + i + "@ee");
            mailTemp.setContent("oo");
            MailUtil.sendMail(mail);

        }
        MailUtil.saveOriginMailRecord(mail);

    }
}
