package com.wso2.org;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;

import static com.itextpdf.text.html.HtmlTags.FONT;


public class GenerateReport {
    public static void main(String[] args) throws Exception{


        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/open-pr-requests", "root", "wso2123");
        Statement stmt = conn.createStatement();

                /* Define the SQL query */
        ResultSet openPr = stmt.executeQuery("SELECT distinct Product,count(PullUrl) as countPR FROM RetrieveOpenPR where OpenWeeks>1 group by Product having countPR order by countPR desc;");



                /*  Initialize PDF documents - logical objects */
        Document openPRreport = new Document(PageSize.A2);
        PdfWriter.getInstance(openPRreport, new FileOutputStream("OpenPRreport.pdf"));
        //BaseFont bf = BaseFont.createFont(FONT,);
        //Font font = new Font(bf, 12);
        openPRreport.open();
        openPRreport.setMargins(300,300,300,300);
        //Paragraph p = new Paragraph("  Number of Open PRs more than a week  ");
        //openPRreport.add(p);

//        Paragraph p = new Paragraph();
//        p.add("This is my paragraph 1");
//        p.setAlignment(Element.ALIGN_CENTER);


        Font f = new Font();
        f.setStyle(Font.BOLD);
        f.setSize(20);


        openPRreport.add(new Paragraph("This is my paragraph 3", f));
        openPRreport.addTitle("Number of open PR more than a week");

        //openPRreport.add(new AreaBreak());
        openPRreport.newPage();



        //Define number if columns
        PdfPTable openPRtable = new PdfPTable(2);


        //create a cell object
        PdfPCell tablecell;


        ArrayList<String> productName = new ArrayList<String>();

        while (openPr.next()) {

              String nameProduct = openPr.getString("Product");
              productName.add(nameProduct);

            tablecell = new PdfPCell(new Phrase(nameProduct));
            openPRtable.addCell(tablecell);


            String countPRMoreWeek = Integer.toString(Integer.parseInt(openPr.getString("countPR")));
            tablecell = new PdfPCell(new Phrase(countPRMoreWeek));
            openPRtable.addCell(tablecell);


        }
        openPr.close();




        for (String product : productName) {



                /*  Initialize PDF documents - logical objects */
            Document openPRAllreport = new Document(PageSize.A2);
            PdfWriter.getInstance(openPRAllreport, new FileOutputStream(product+".pdf"));
            openPRAllreport.open();


            //Define number if columns
            PdfPTable openPRAlltable = new PdfPTable(9);


            //create a cell object
            PdfPCell tablecell1=new PdfPCell((new Phrase("This is a test document")));
            tablecell1.setBackgroundColor(BaseColor.ORANGE);


            ResultSet AllOpenPR=stmt.executeQuery("select * from RetrieveOpenPR where product='"+product+"'");
            while(AllOpenPR.next())
            {
                tablecell1=new PdfPCell(new Phrase(product));
                openPRAlltable.addCell(tablecell1);

                String repoUrl=AllOpenPR.getString("Repourl");
                tablecell1 = new PdfPCell(new Phrase(repoUrl));
                openPRAlltable.addCell(tablecell1);

                String gitID=AllOpenPR.getString("GitId");
                tablecell1 = new PdfPCell(new Phrase(gitID));
                openPRAlltable.addCell(tablecell1);

                String pullUrl=AllOpenPR.getString("PullUrl");
                tablecell1 = new PdfPCell(new Phrase(pullUrl));
                openPRAlltable.addCell(tablecell1);

                String openHours=Integer.toString(Integer.parseInt(AllOpenPR.getString("OpenHours")));
                tablecell1 = new PdfPCell(new Phrase(openHours));
                openPRAlltable.addCell(tablecell1);

                String openDays=Integer.toString(Integer.parseInt(AllOpenPR.getString("OpenDays")));
                tablecell1 = new PdfPCell(new Phrase(openDays));
                openPRAlltable.addCell(tablecell1);

                String openWeeks=Integer.toString(Integer.parseInt(AllOpenPR.getString("OpenWeeks")));
                tablecell1 = new PdfPCell(new Phrase(openWeeks));
                openPRAlltable.addCell(tablecell1);

                String openMonths=Integer.toString(Integer.parseInt(AllOpenPR.getString("OpenMonths")));
                tablecell1 = new PdfPCell(new Phrase(openMonths));
                openPRAlltable.addCell(tablecell1);

                String openYears=Integer.toString(Integer.parseInt(AllOpenPR.getString("OpenYears")));
                tablecell1 = new PdfPCell(new Phrase(openYears));
                openPRAlltable.addCell(tablecell1);


            }
           AllOpenPR.close();
            openPRAllreport.add(openPRAlltable);
            openPRAllreport.close();

        }


                /* Attach report table to PDF */
        openPRreport.add(openPRtable);
        openPRreport.close();

                /* Close all DB related objects */
        stmt.close();
        conn.close();


    }



}
