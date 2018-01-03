package com.wso2.org;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailAttachmentSender {

    public static void sendEmailWithAttachments(String host, String port,
                                                final String userName, final String password, String toAddress,
                                                String subject, String message, String[] attachFiles)
            throws AddressException, MessagingException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        
        for (int i = 0; i < toAddress.length(); i++) {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        }

        
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds attachments
        if (attachFiles != null) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();
                if(filePath != null) {
                    try {
                        attachPart.attachFile(filePath);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    multipart.addBodyPart(attachPart);
                    // sets the multi-part as e-mail's content
                    msg.setContent(multipart);
                    // sends the e-mail
                    Transport.send(msg);
                    System.out.println("Email sent.");
                }
            }
        }
    }

    /**
     * Test sending e-mail with attachments
     */
    public static void main(String[] args) throws IOException {
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "senthanprasanth007@gmail.com";
        String password = "";

        ReadConfigureFile credentials = new ReadConfigureFile();

        
        // message info

        ArrayList<String> mailTo = new ArrayList<String>();

        String groupEmail = "";

        for (int y = 1; y < 3; y++) {
            groupEmail = credentials.getGroupEmail(y);
            mailTo.add(groupEmail + "@gmail.com");
            String subject = "open PRs-" + groupEmail;

            String message = "https://identity-gateway.cloud.wso2.com/t/wso2internal928/gitopenprdashboard/";

            //System.out.println(groupEmail);

//            if (groupEmail.equals("senthanyadhury")) {
            String[] attachFiles = new String[1];

            if (groupEmail.equals("senthanyadhury98")) {
                File file = new File("reports/Ballerina.pdf");
                if (file.exists()) {
                    attachFiles[0] = "reports/Ballerina.pdf";
                }

            } else if (groupEmail.equals("senthanprasanth007")) {
                File file = new File("reports/Cloud.pdf");
                if (file.exists()) {
                    attachFiles[0] = "reports/Cloud.pdf";                }
                }

                try {
                    sendEmailWithAttachments(host, port, mailFrom, password, mailTo.get(y - 1),
                            subject, message, attachFiles);
                } catch (Exception ex) {
                    System.out.println("Could not send email.");
                    ex.printStackTrace();
                }
            }
        }
    }
