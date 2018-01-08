package com.wso2.org;

/**
 *
 * @author Rasmi_G
 */
import java.io.StringWriter;
import java.sql.*;
import java.util.*;
import org.apache.velocity.*;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;


public class Velocity1{
    static HashMap map;
    public static void main(String[] args) throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.init();

            //Derby database url
            String url=   "jdbc:mysql://localhost:3306/open-pr-requests?useSSL=false";
            //Database connection details with the username and password
            Connection conn = DriverManager.getConnection(url,"root","wso2123");
            Statement stmt = conn.createStatement();
            ResultSet rs;
            //the product is table in the Derby Sample database
            rs = stmt.executeQuery("SELECT * FROM product");
            String id ;
            String cost1;
            ArrayList list = new ArrayList();
            //Retrieving the data and storing into a List in the form of map
            while ( rs.next() ) {
                map = new HashMap();
                id = rs.getString("Product");
                cost1=rs.getString("RepoName");
                map.put("Pro", id);
                map.put("Repo",cost1); }
            list.add(map);
            int count=list.size();
            ArrayList l=new ArrayList();
            l.add(list.get(count-1));
            //For Loading the velocity page
            Properties p = new Properties();
            //loading the template engine path
            p.setProperty("file.resource.loader.path", "/home/senthan/Documents/apacheVelocity/src/main/resources");
            //Storing  the data in the Velocity Context
            VelocityContext context = new VelocityContext();
            context.put("prdList", l);
            //Initialize velocity run time engine through method  init()
            Velocity.init(p);
            Template t = Velocity.getTemplate("Extract.vm");
            StringWriter writer = new StringWriter();
            //merge() is a  method of the Template class.
            //The usage of merge() is  for merging  the VelocityContext class object to produce the output.
            t.merge(context, writer);
            System.out.println(writer.toString());

        conn.close();

    }
}