package com.wso2.org;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class GenerateEmail
{
    public static void main( String[] args )
    {
        final String senderEmail="senthanprasanth007@gmail.com";
        final String password="";
        Properties props =new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");
        properties.put("mail.smtp.ssl.trust", "tygra.wso2.com");

        Session session=Session.getInstance(props,new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(senderEmail,password);
            }
        });

        try{
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress("senthan@wso2.com"));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("senthan@wso2.com"));
            message.setSubject("Hello message");
            message.setText("Hello prasanth!!!!!!!");

            Transport.send(message);

            System.out.println("done");

        }
        catch (MessagingException r){
            throw new RuntimeException(r);
        }


    }
}
