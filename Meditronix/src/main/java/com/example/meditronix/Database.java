package com.example.meditronix;
import java.sql.*;

public class Database {

     public Connection dbConnect()  {
         try {
             Class.forName("com.mysql.jdbc.Driver");
             String url = "Replace with url given in messenger pinned comment";
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

    public void deleteMedicine(String id, Connection con) throws SQLException {

        String sql = "Delete From shop_inventory where serial_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,id);

        int rowsDeleted = stmt.executeUpdate();

        //Replace by notification on Hpanel
        if (rowsDeleted > 0) {
            System.out.println("Medicine with serial ID " + id + " deleted successfully!");
        } else {
            System.out.println("No medicine found with serial ID " + id);
        }

        stmt.close();
        con.close();
    }


}
