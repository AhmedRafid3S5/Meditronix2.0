package com.example.meditronix;
import java.sql.*;

public class Database {

     public Connection dbConnect()  {
         try {
             Class.forName("com.mysql.jdbc.Driver");
             String url = "jdbc:mysql://database-1.c5ymicw8wwm5.ap-southeast-2.rds.amazonaws.com:3306/mydb?characterEncoding=UTF-8";
             String username = "admin";
             String password = "admin1234";

             Connection con = DriverManager.getConnection(url, username, password);
             System.out.println("Connected to database successfully");
             return con;

         }
         catch (Exception e) {
             System.out.println("Error: " + e.getMessage());
         }

         return null;
     }

    public ResultSet showInventory() throws SQLException {

         Connection con = dbConnect();
         Statement stmt = con.createStatement();

         String sql = "SELECT * FROM shop_inventory; ";
         ResultSet rs = stmt.executeQuery(sql);

         return rs;

    }

}
