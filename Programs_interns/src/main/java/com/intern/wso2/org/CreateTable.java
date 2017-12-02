package com.intern.wso2.org;

        import java.sql.*;

public class CreateTable {


    public static void main(String[] args) {

        String url="jdbc:mysql://localhost:3306/demo?useSSL=false"; //demo database Name
        String user="root";
        String password="wso2123";

        try {
            Connection con=DriverManager.getConnection(url,user,password);


            try{
                Statement stmt=con.createStatement();
                String table="create table employee(name varchar(20),designation varchar(20),salary double)";
                stmt.executeUpdate(table);
                System.out.println("Table created successfully!!!!!!");
            }

            catch (SQLException e) {
                System.out.println("Table already exists!!!!");
            }
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }





}



