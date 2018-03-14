        /*
         *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.Properties;
        import javax.mail.Authenticator;
        import javax.mail.Message;
        import javax.mail.MessagingException;
        import javax.mail.Multipart;
        import javax.mail.PasswordAuthentication;
        import javax.mail.Session;
        import javax.mail.Transport;
        import javax.mail.internet.InternetAddress;
        import javax.mail.internet.MimeBodyPart;
        import javax.mail.internet.MimeMessage;
        import javax.mail.internet.MimeMultipart;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        public class BallerinaOpenPRsRetriever {
            private static Logger logger = LoggerFactory.getLogger(BallerinaOpenPRsRetriever.class);

            private static void sendEmailOpenPRsBallerina(String host, String port,
                                                         final String userName, final String password, String toAddress, ArrayList<String> ccList,
                                                         String subject, String message)
                    throws  MessagingException {
                // sets SMTP server properties
                Properties properties = new Properties();
                properties.put("mail.smtp.host", host);
                properties.put("mail.smtp.port", port);
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.user", userName);
                properties.put("mail.password", password);
                properties.put("mail.smtp.ssl.trust", "tygra.wso2.com");


                Authenticator auth = new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                };

                // Get the default Session object.
                Session session = Session.getInstance(properties, auth);

                // creates a new e-mail message
                Message msg = new MimeMessage(session);

                msg.setFrom(new InternetAddress(userName));

                for (int i = 0; i < toAddress.length(); i++) {
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
                }

                // Set CC: header field of the header.
                for (String aCcList : ccList) {
                    msg.addRecipient(Message.RecipientType.CC,
                            new InternetAddress(aCcList));
                }




                msg.setSubject(subject);
                msg.setSentDate(new Date());

                // creates message part
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(message, "text/html");

                // creates multi-part
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);


                // sets the multi-part as e-mail's content
                msg.setContent(multipart);
                // sends the e-mail
                Transport.send(msg);
                System.out.println("Email sent.");



            }

            /**
             *  sending open PRs of ballerina in email body
             */
            public void sendOpenPRsBallerina() throws Exception {

                ReadConfigureFile credentials= new ReadConfigureFile();

                // SMTP info
                String host = credentials.getsmtpHost();
                String port = credentials.getsmtpPort();
                String mailFrom = credentials.getmailFrom();
                String mailPassword = credentials.getmailPassword();
                String url = credentials.getDatabaseConn();
                Connection conn = DriverManager.getConnection(url, credentials.getUser(), credentials.getPassword());
                Statement create = conn.createStatement();


                String openPRSummaryProduct = "SELECT Product,count(PullUrl) as countPR FROM RetrieveOpenPR where Product='Ballerina'";

                StringBuilder prProduct = new StringBuilder();


                prProduct.append("<html><head><head>\n" + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" + "  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" + "  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\n" + "</head><style>    table {\n" + "        font-family: arial, sans-serif;\n" + "width: 60%;\n" + "    }  th {\n" + "        border: 2px solid;\n" + "        padding: 8px;\n" + "        font-size: 14px;\n" + "    }\n" + "    td{\n" + "            border: 2px solid black;\n" + "            padding: 8px;\n" + "            font-size: 14px;\n" + "    }\n" + "\n" + "   }</style></head><body>"
                        + "<table style='border:2px solid black;border-collapse: collapse;' class='table-responsive'>");

                prProduct.append("<th bgcolor=\"#4f5154\"><b><font color=\"#fff\">Product</font></b></th>");
                prProduct.append("<th bgcolor=\"#4f5154\"><b><font color=\"#fff\">No. of All Open PRs</font></b></th>");

                ResultSet perProductPRSummary = create.executeQuery(openPRSummaryProduct);

                ArrayList<String> productName = new ArrayList<String>();

                while (perProductPRSummary.next()) {

                    String product=perProductPRSummary.getString("Product");
                    productName.add(product);

                    prProduct.append("<tr>");
                    prProduct.append("<td><b>");
                    prProduct.append(product);
                    prProduct.append("</b></td>");

                    prProduct.append("<td><center><b>");
                    prProduct.append(perProductPRSummary.getString("countPR"));
                    prProduct.append("</b></center></td>");


                    prProduct.append("<tr>");
                }

                prProduct.append("</table><br><br><br><br></body></html>");


                //Adding values for the map with key-productName value-emailIdOfGroup
                Map<String,String> emailGroupMap = new HashMap<String, String>();
                emailGroupMap.put("Ballerina","ballerina-group@wso2.com");


                //Adding values for the map with key-productName value-Subject
                Map<String,String> subjectPropertyMap = new HashMap<String, String>();
                DateGenerator printDate=new  DateGenerator();
                String currentDate=printDate.GetCurrentDate();

                subjectPropertyMap.put("Ballerina","[Open PR's] - Ballerina  "+currentDate);


                ArrayList<String> ccList = new ArrayList<String>();

               // StringBuilder allOpenPR= new StringBuilder();

                for (String productList : productName) {

                    ResultSet SummaryOpenPR;
                    StringBuilder messageNew = new StringBuilder();

                    SummaryOpenPR = create.executeQuery("select * from RetrieveOpenPR where Product='"+productList+"' order by OpenWeeks desc");

                    messageNew.append(prProduct);
                    int x = messageAppend(SummaryOpenPR, messageNew);

                    try {
                        if (x != 0) {


                            sendEmailOpenPRsBallerina(host, port, mailFrom, mailPassword, emailGroupMap.get(productList),ccList,subjectPropertyMap.get(productList), messageNew.toString());
                            System.out.println(subjectPropertyMap.get(productList));

                        }
                        else{
                            logger.info("There is no open PRs for Ballerina at the moment");
                        }

                    } catch (Exception ex) {
                        System.out.println("Could not send email.");

                    }
                }

            }


            private static int messageAppend(ResultSet summaryOpenPR, StringBuilder messageNew) throws SQLException {
                int noOfOpenPrs = 0;
                messageNew.append("<html><head><head>\n" + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" + "  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" + "  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\n" + "</head><style>th {\n" + "        border: 2px solid black;\n" + "        padding: 8px;\n" + "        font-size: 14px;\n" + "    }\n" + "    td{\n" + "            border: 2px solid black;\n" + "            padding: 8px;\n" + "            font-size: 14px;\n" + "    }\n" + "\n" + "    tr:nth-child(even) {\n" + "        background-color: #dddddd;\n" + "    }</style></head><body>"
                        + "<table  style='border:2px solid black; width: 80%;border-collapse: collapse;'  class='table-responsive'>");



                messageNew.append("<th bgcolor=\"#4f5154\"'><b><font color=\"#fff\">Product</font></b></th>");
                messageNew.append("<th bgcolor=\"#4f5154\"'><b><font color=\"#fff\">Git ID</font></b></th>");
                messageNew.append("<th bgcolor=\"#4f5154\"' width=\"40%\"><b><font color=\"#fff\">PR URL</font></b></th>");
                messageNew.append("<th bgcolor=\"#4f5154\"'><b><font color=\"#fff\">Open Days</font></b></th>");
                messageNew.append("<th bgcolor=\"#4f5154\"'><b><font color=\"#fff\">Open Weeks</font></b></th>");
                messageNew.append("<th bgcolor=\"#4f5154\"'><b><font color=\"#fff\">Open Months</font></b></th>");



                while (summaryOpenPR.next()) {
                    messageNew.append("<tr>");
                    messageNew.append("<td><b>");
                    messageNew.append(summaryOpenPR.getString("Product"));
                    messageNew.append("</b></td>");

                    messageNew.append("<td><b>");
                    messageNew.append(summaryOpenPR.getString("GitId"));
                    messageNew.append("</b></td>");

                    messageNew.append("<td width=\"40%\"><b>");
                    messageNew.append(summaryOpenPR.getString("PullUrl"));
                    messageNew.append("</b></td>");

                    messageNew.append("<td><center><b>");
                    messageNew.append(summaryOpenPR.getString("OpenDays"));
                    messageNew.append("</b></center></td>");

                    messageNew.append("<td><center><b>");
                    messageNew.append(summaryOpenPR.getString("OpenWeeks"));
                    messageNew.append("</b></center></td>");

                    messageNew.append("<td><center><b>");
                    messageNew.append(summaryOpenPR.getString("OpenMonths"));
                    messageNew.append("</b></center></td>");

                    messageNew.append("<tr>");

                    noOfOpenPrs++;


                }
                messageNew.append("</table><br><br><h2 style=\"color:#2196F3\">To view the Dashboard<h3>https://identity-gateway.cloud.wso2.com/t/wso2internal928/gitopenprdashboard/</h2></h3></h3></body></html>");
                return noOfOpenPrs;

            }



        }



