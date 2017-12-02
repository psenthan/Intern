package com.intern.wso2.org;




        import java.sql.*;




public class JDBC_Connection {


    public static void main(String[] args) {

        try {
            Connection myConn=DriverManager.getConnection("jdbc:mysql://localhost:3306/demo?useSSL=false","root","wso2123"); //Get a connection to employee
            Statement myst=myConn.createStatement(); //create a statement object

            ResultSet result=myst.executeQuery("select * from employee"); // Execute sql query

            //Process the result set
            while(result.next())
            {
                System.out.println(result.getString("name"));
                System.out.println(result.getString("designation"));
                System.out.println(result.getString("salary"));
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
