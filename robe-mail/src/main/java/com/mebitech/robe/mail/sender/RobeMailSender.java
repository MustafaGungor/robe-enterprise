package com.mebitech.robe.mail.sender;

import com.mebitech.robe.mail.MailItem;

/**
 * Created by recepkoseoglu on 3/22/17.
 */
public interface RobeMailSender {
    void sendMail(MailItem mailItem);
}
