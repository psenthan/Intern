package com.wso2.org;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VelocityTemplateGeneration {
    static HashMap map;
    static ArrayList list;
    static ArrayList senthanlist,yadhurylist;

    static ArrayList<ArrayList<String>> alllist = new ArrayList<ArrayList<String>>();

    public static void TemplateGeneration() throws Exception {
        ReadConfigureFile credentials = new ReadConfigureFile();

        String url = credentials.getDatabaseConn();
        Connection conn = DriverManager.getConnection(url, credentials.getUser(), credentials.getPassword());
        Statement stmt = conn.createStatement();
        ResultSet rs;

        rs = stmt.executeQuery("SELECT distinct Product,count(PullUrl) as countPR FROM RetrieveOpenPR where OpenWeeks>1 group by Product having countPR order by countPR desc;");

        String product;
        int CountPR;
        list = new ArrayList();


        ArrayList<String> productName = new ArrayList<String>();

        while (rs.next()) {
            map = new HashMap();
            product = rs.getString("Product");

            productName.add(product);
            CountPR = rs.getInt("countPR");
            map.put("Product", product);
            map.put("CountPR", CountPR);
            //System.out.println(map);


            list.add(map);
        }



        yadhurylist=new ArrayList();
        senthanlist=new ArrayList();

        for (String prod : productName) {

            ResultSet SummaryPR;
            SummaryPR = stmt.executeQuery("select * from RetrieveOpenPR where product='" + prod + "' and OpenWeeks>1 order by OpenWeeks desc");

            String repoUrl;
            String gitId;
            String prUrl;
            int openHours;
            int openDays;
            int openWeeks;
            int openMonths;
            int openYears;

            while (SummaryPR.next()) {

                HashMap map1 = new HashMap();
                product = SummaryPR.getString("Product");
                repoUrl = SummaryPR.getString("RepoUrl");
                gitId = SummaryPR.getString("GitId");
                prUrl = SummaryPR.getString("PullUrl");
                openHours = SummaryPR.getInt("OpenHours");
                openDays = SummaryPR.getInt("OpenDays");
                openWeeks = SummaryPR.getInt("OpenWeeks");
                openMonths = SummaryPR.getInt("OpenMonths");
                openYears = SummaryPR.getInt("OpenYears");

                map1.put("Product", product);
                map1.put("RepoUrl", repoUrl);
                map1.put("GitId", gitId);
                map1.put("PullUrl", prUrl);
                map1.put("OpenHours", openHours);
                map1.put("OpenDays", openDays);
                map1.put("OpenWeeks", openWeeks);
                map1.put("OpenMonths", openMonths);
                map1.put("OpenYears", openYears);

                //list1.add(map);


                 if(prod.equals(("yadhury"))){
                        yadhurylist.add(map1);

                }
                else if(prod.equals(("senthan"))){
                        senthanlist.add(map1);
                }
                else{


                }


            }


        }


        alllist.add(yadhurylist);
        alllist.add(senthanlist);

        for (ArrayList x: alllist
             ) {
           // System.out.println("1");
          // System.out.println(x);
        }

    }
}
