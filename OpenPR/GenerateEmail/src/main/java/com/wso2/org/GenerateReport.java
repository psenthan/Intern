package com.wso2.org;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;


public class GenerateReport {
    public static void main(String[] args) throws Exception{


        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/open-pr-requests", "root", "wso2123");
        Statement stmt = conn.createStatement();

        ResultSet openPr = stmt.executeQuery("SELECT distinct Product,count(PullUrl) as countPR FROM RetrieveOpenPR where OpenWeeks>1 group by Product having countPR order by countPR desc;");



        Document openPRreport = new Document(PageSize.A2);
        PdfWriter.getInstance(openPRreport, new FileOutputStream("OpenPRreport.pdf"));

        openPRreport.open();

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, BaseColor.BLUE);
        Chunk chunk = new Chunk("Number of open PR more than a week", font);

        openPRreport.add(chunk);

        openPRreport.add(new Phrase("\n"));



        font.isBold();

=======
        
        openPRreport.open();
        
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, BaseColor.BLUE);
        Chunk chunk = new Chunk("Number of open PR more than a week", font);
>>>>>>> 0d1e21978bfe863563d16befdd10d77db815bb92

        openPRreport.add(chunk);

<<<<<<< HEAD
        PdfPTable openPRtable = new PdfPTable(2);
        openPRtable.setWidthPercentage(80);
        openPRtable.addCell("Product");
        openPRtable.addCell("Open PRs");
        openPRtable.setHeaderRows(1);
        openPRtable.setHeaderRows(Element.ALIGN_CENTER);
        openPRtable.setHorizontalAlignment(Element.ALIGN_CENTER);
        openPRtable.setSpacingAfter(20);
=======
        openPRreport.add(new Phrase("\n"));
        font.isBold();
        
        //Define number if columns
        PdfPTable openPRtable = new PdfPTable(2);
        openPRtable.setSpacingAfter(20);
        
        //create a cell object
>>>>>>> 0d1e21978bfe863563d16befdd10d77db815bb92
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

        File directory = new File("/home/senthan/Documents/GenerateEmail/target/reports");
        FileUtils.cleanDirectory(directory);

        ArrayList<PdfPTable> allOpenPRList= new ArrayList<PdfPTable>();

        for (String product : productName) {

            // PdfReader reader=new PdfReader(OpenPRpdfReport);

<<<<<<< HEAD
=======

            /*  Initialize PDF documents - logical objects */
>>>>>>> 0d1e21978bfe863563d16befdd10d77db815bb92
            Document openPRAllreport = new Document(PageSize.A2);
            PdfWriter.getInstance(openPRAllreport, new FileOutputStream("target/reports/"+ product + ".pdf"));
            openPRAllreport.open();

            Font font1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, BaseColor.BLUE);
            Chunk chunk1 = new Chunk(product, font1);

            openPRAllreport.add(chunk1);

            openPRAllreport.add(new Phrase("\n"));
            font.isBold();



<<<<<<< HEAD
            PdfPTable openPRAlltable = new PdfPTable(9);
            openPRAlltable.setWidthPercentage(100);
            openPRAlltable.addCell("Product");
            openPRAlltable.addCell("Repository Url");
            openPRAlltable.addCell("Git_ID");
            openPRAlltable.addCell("PR Url");
            openPRAlltable.addCell("Open Hours");
            openPRAlltable.addCell("Open Days");
            openPRAlltable.addCell("Open Weeks");
            openPRAlltable.addCell("Open Months");
            openPRAlltable.addCell("Open Years");
            openPRAlltable.setHeaderRows(1);

            PdfPCell tablecell1;



            ResultSet AllOpenPR = stmt.executeQuery("select * from RetrieveOpenPR where product='" + product + "' and OpenWeeks>1");
            if(!AllOpenPR.wasNull()) {
                while (AllOpenPR.next()) {
                    tablecell1 = new PdfPCell(new Phrase(product));
                    openPRAlltable.addCell(tablecell1);
                    
                    
            Font font1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, BaseColor.ORANGE);
            Chunk chunk1 = new Chunk(product, font1);
            openPRAllreport.add(chunk1);
            openPRAllreport.add(new Phrase("\n"));
            font.isBold();

            
            
            //Define number if columns
            PdfPTable openPRAlltable = new PdfPTable(9);
            PdfPCell tablecell1;
      


            ResultSet AllOpenPR=stmt.executeQuery("select * from RetrieveOpenPR where product='"+product+"'");
            
            if(!AllOpenPR.wasNull()){
            while(AllOpenPR.next())
            {
                tablecell1=new PdfPCell(new Phrase(product));
                openPRAlltable.addCell(tablecell1);


                    String repoUrl = AllOpenPR.getString("Repourl");
                    tablecell1 = new PdfPCell(new Phrase(repoUrl));
                    openPRAlltable.addCell(tablecell1);

                    String gitID = AllOpenPR.getString("GitId");
                    tablecell1 = new PdfPCell(new Phrase(gitID));
                    openPRAlltable.addCell(tablecell1);

                    String pullUrl = AllOpenPR.getString("PullUrl");
                    tablecell1 = new PdfPCell(new Phrase(pullUrl));
                    openPRAlltable.addCell(tablecell1);

                    String openHours = Integer.toString(Integer.parseInt(AllOpenPR.getString("OpenHours")));
                    tablecell1 = new PdfPCell(new Phrase(openHours));
                    openPRAlltable.addCell(tablecell1);

                    String openDays = Integer.toString(Integer.parseInt(AllOpenPR.getString("OpenDays")));
                    tablecell1 = new PdfPCell(new Phrase(openDays));
                    openPRAlltable.addCell(tablecell1);

                    String openWeeks = Integer.toString(Integer.parseInt(AllOpenPR.getString("OpenWeeks")));
                    tablecell1 = new PdfPCell(new Phrase(openWeeks));
                    openPRAlltable.addCell(tablecell1);

                    String openMonths = Integer.toString(Integer.parseInt(AllOpenPR.getString("OpenMonths")));
                    tablecell1 = new PdfPCell(new Phrase(openMonths));
                    openPRAlltable.addCell(tablecell1);

                    String openYears = Integer.toString(Integer.parseInt(AllOpenPR.getString("OpenYears")));
                    tablecell1 = new PdfPCell(new Phrase(openYears));
                    openPRAlltable.addCell(tablecell1);
                }

                openPRAllreport.add(openPRtable);
                openPRAllreport.add(openPRAlltable);


                allOpenPRList.add(openPRAlltable);

                openPRAllreport.close();

            }
                openPRAllreport.add(openPRtable);
                openPRAllreport.add(openPRAlltable);
                allOpenPRList.add(openPRAlltable);
                openPRAllreport.close();
            }
                 AllOpenPR.close();
            
        }

            AllOpenPR.close();

        }

        openPRreport.add(openPRtable);

        openPRreport.add(new Phrase("\n"));
        openPRreport.add(new Phrase("\n"));
        openPRreport.add(new Phrase("\n"));

        for (PdfPTable table: allOpenPRList) {
            openPRreport.add(table);
            openPRreport.add(new Phrase("\n"));
        
        for (PdfPTable table: allOpenPRList) {
            openPRreport.add(table);

        }
        openPRreport.close();

        stmt.close();
        conn.close();


    }



}
