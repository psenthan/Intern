    /*
     *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
     *
     *  WSO2 Inc. licenses this file to you under the Apache License,
     *  Version 2.0 (the "License"); you may not use this file except
     *  in compliance with the License.
     *  You may obtain a copy of the License at
     *
     *    http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing,
     * software distributed under the License is distributed on an
     * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
     * KIND, either express or implied.  See the License for the
     * specific language governing permissions and limitations
     * under the License.
     */
    package com.wso2.org;

    import org.apache.velocity.Template;
    import org.apache.velocity.VelocityContext;
    import org.apache.velocity.app.VelocityEngine;

    import java.io.StringWriter;
    import java.util.*;

    import javax.mail.Authenticator;
    import javax.mail.Message;
    import javax.mail.Multipart;
    import javax.mail.PasswordAuthentication;
    import javax.mail.Session;
    import javax.mail.Transport;
    import javax.mail.internet.InternetAddress;
    import javax.mail.internet.MimeBodyPart;
    import javax.mail.internet.MimeMessage;
    import javax.mail.internet.MimeMultipart;



    public class EmailAttachmentSender {



        public static void sendEmailWithAttachments(String host, String port,
                                                    final String userName, final String password, String toAddress,
                                                    String subject, String message)
                throws Exception {
            // sets SMTP server properties
            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.user", userName);
            properties.put("mail.password", password);
            //properties.put("mail.smtp.ssl.trust", "tygra.wso2.com");




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

            // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();


            //System.out.println(out.toString());

            // creates multi-part
            Multipart multipart = new MimeMultipart();

            VelocityEngine ve = new VelocityEngine();
            ve.init();



            Template t = ve.getTemplate("./src/main/resources/Extract.vm");
            VelocityContext context = new VelocityContext();
            context.put("list", VelocityTemplateGeneration.list);




            for (int j = 0; j < VelocityTemplateGeneration.alllist.size(); j++) {
                ArrayList listElement = VelocityTemplateGeneration.alllist.get(j);
                System.out.println(listElement);
                for (int i = 0; i < listElement.size(); i++) {
                    HashMap hashMap = (HashMap) listElement.get(i);
                    //System.out.println(hashMap);
                    String productName = (String) hashMap.get("Product");
                    System.out.println(productName);



                switch (productName){

                    case "yadhury":
                        context.put("yadhury",VelocityTemplateGeneration.yadhurylist);
                       // System.out.println(VelocityTemplateGeneration.yadhurylist);
                        break;

                    case "senthan":
                        context.put("list1",VelocityTemplateGeneration.senthanlist);
                       //System.out.println(VelocityTemplateGeneration.senthanlist);

                        break;

                    default:


                }

                }
            }
            StringWriter out = new StringWriter();
            t.merge(context,out);


            messageBodyPart.setContent(out.toString(), "text/html");



            // sets the multi-part as e-mail's content

                multipart.addBodyPart(messageBodyPart);
                msg.setContent(multipart, "text/html");
                // sends the e-mail
               //Transport.send(msg);
            out.flush();
                System.out.println("Email sent.");
            }


            /**
             * Test sending e-mail with attachments
             */
        public static void main(String args[]) throws Exception {

            VelocityTemplateGeneration emailTemplate = new VelocityTemplateGeneration();
             emailTemplate.TemplateGeneration();

            System.out.println(VelocityTemplateGeneration.senthanlist);

            ReadConfigureFile credentials = new ReadConfigureFile();
            // SMTP info
            String host = credentials.getsmtpHost();
            String port = credentials.getsmtpPort();
            String mailFrom = credentials.getmailFrom();
            String mailPassword = credentials.getmailPassword();

            GenerateDate printDate = new GenerateDate();
            String currentDate = printDate.GetCurrentDate();


            ArrayList<String> mailTo = new ArrayList<String>();

            String groupEmail = "";
            String subject1 = "";
            String subject = "";



            for (int y = 1; y < 3; y++) {
                groupEmail = credentials.getGroupEmail(y);
                System.out.println(groupEmail);
                mailTo.add(groupEmail + "@yahoo.com");

                subject1 = "open PRs-" + groupEmail;

                if (subject1.contains("_group") && !subject1.contains("engineering-group")) {
                    subject = subject1.replaceAll("_group", "") + " - " + currentDate;
                    System.out.println(subject);

                } else {
                    subject = "All open PRs - " + currentDate;
                    System.out.println(subject);

                }

                String message="";


                    try {
                        sendEmailWithAttachments(host, port, mailFrom, mailPassword, mailTo.get(y - 1), subject, message);
                       // System.out.println(mailTo.get(y - 1));
                    } catch (Exception ex) {
                        System.out.println("Could not send email.");
                        ex.printStackTrace();
                    }

                }
            }



            }



