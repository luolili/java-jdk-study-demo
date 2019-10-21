package com.luo.ptn.creational.prototype;

import java.text.MessageFormat;

public class MailUtil {

    public static void sendMail(Mail mail) {
        String outputContent = "像{0}同学，地址{1},发送：{2}";
        System.out.println(MessageFormat.format(outputContent, mail.getName(),
                mail.getMailAddress(), mail.getContent()));

    }

    public static void saveOriginMailRecord(Mail mail) {
        System.out.println("save origi mail:content:" + mail.getContent());
    }
}
