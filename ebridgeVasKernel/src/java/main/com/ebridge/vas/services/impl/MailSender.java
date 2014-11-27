package com.ebridge.vas.services.impl;

import com.ebridge.vas.dao.OutboundMsgDao;
import com.ebridge.vas.model.OutboundMsg;
import com.ebridge.vas.util.DatabaseException;
import org.apache.commons.mail.EmailException;

/**
 * @author david@ebridgevas.com
 *
 */
public class MailSender {

    private SendMailTLS sendMail;

    public MailSender() {
        sendMail = new SendMailTLS();
    }

    public void send(){

        try {

            for ( OutboundMsg message : OutboundMsgDao.getPendingMessages("EMAIL")) {

                sendMail.send(
                        new String[]{message.getDestinationId()},
                        "Telecel Web Access Activation Code",
                        message.getPayload());
                OutboundMsgDao.updateStatus(message.getMessageId(), "SENT");
            }
        } catch (EmailException e) {
            e.printStackTrace();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        while (true) {
            new MailSender().send();
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
