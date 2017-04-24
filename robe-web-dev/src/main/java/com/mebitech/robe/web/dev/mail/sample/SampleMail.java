package com.mebitech.robe.web.dev.mail.sample;

import com.mebitech.robe.mail.MailItem;
import com.mebitech.robe.mail.sender.RobeMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by recepkoseoglu on 3/24/17.
 */
public class SampleMail {

    @Qualifier("mailName")
    @Autowired
    RobeMailSender robeMailSender;

    public void sendMail() {
        MailItem item = new MailItem();
        item.setBody("<h1>Hello world</h1>");
        item.setTitle("Hello");
        item.setReceivers("name.surname@company.com");
        item.setEvent(new Event());
        robeMailSender.sendMail(item);
    }
}
