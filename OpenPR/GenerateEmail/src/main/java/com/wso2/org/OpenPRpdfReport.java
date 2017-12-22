package com.wso2.org;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.sql.*;


public class OpenPRpdfReport {
    public static void main(String[] args) throws Exception{


        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/open-pr-requests", "root", "wso2123");
        Statement stmt = conn.createStatement();

                /* Define the SQL query */

        ResultSet openPr = stmt.executeQuery("SELECT distinct Product,count(PullUrl) as countPR FROM RetrieveOpenPR where OpenWeeks>1 group by Product having countPR order by countPR desc;");

                /*  Initialize PDF documents - logical objects */
        Document openPRreport = new Document();
        PdfWriter.getInstance(openPRreport, new FileOutputStream("OpenPRreport.pdf"));
        openPRreport.open();
        openPRreport.addTitle("Number of Open PR more than a week");


        //Define number if columns
        PdfPTable openPRtable = new PdfPTable(2);


        //create a cell object
        PdfPCell tablecell;

        tablecell = new PdfPCell(new Phrase("Cell with colspan 5"));
        tablecell.setColspan(3);




        while (openPr.next()) {

            String productName = openPr.getString("Product");
            tablecell=new PdfPCell(new Phrase(productName));
            openPRtable.addCell(tablecell);

            String countPRMoreWeek=Integer.toString(Integer.parseInt(openPr.getString("countPR")));
            tablecell=new PdfPCell(new Phrase(countPRMoreWeek));
            openPRtable.addCell(tablecell);





            }







                /* Attach report table to PDF */
        openPRreport.add(openPRtable);
        openPRreport.close();

                /* Close all DB related objects */
        openPr.close();
        stmt.close();
        conn.close();

    }
}
