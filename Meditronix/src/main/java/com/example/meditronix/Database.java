package com.example.meditronix;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Database {

     public Connection dbConnect()  {
         try {
             Class.forName("com.mysql.jdbc.Driver");
             //String url = "jdbc:mysql://database-1.czywou6sao7o.ap-southeast-2.rds.amazonaws.com:3306/mydb?characterEncoding=UTF-8";
             //String username = "admin";
             String url = "jdbc:mysql://127.0.0.1:3306/meditronix";
             String username = "root";
             String password = "12345";

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
    public String createUniqueID()
    {
        LocalDateTime currentTime = LocalDateTime.now();

        // Define a DateTimeFormatter for the desired timestamp format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime to a string using the formatter

        return currentTime.format(formatter);

    }
    public void addMedicine(Medicine m,Connection con) throws SQLException{

        String sql = "SELECT *\n" +
                "FROM shop_inventory\n" +
                "WHERE `Name` = ?\n" +
                "  AND `Dose` = ?\n" +
                "  AND `expiry` = ?\n" +  // Use the expiry date directly in the new format
                "  AND `type` = ?;";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,m.getName());
        stmt.setString(2,m.getDose());
        stmt.setString(3, m.getExpiry());
        stmt.setString(4,m.getType());

        ResultSet rowsSelected = stmt.executeQuery();

        //if med with the same name
        if (rowsSelected.next()) {
            String updateSQL = "UPDATE `shop_inventory`\n" +
                    "SET \n" +
                    "  `Selling_price` = ?, \n" +
                    "  `Available_Quantity` = ?, \n" +
                    "  `unit_cost` = ?  \n" +
                    "WHERE `Name` = ? AND `Expiry` = ?;";

            PreparedStatement update_stmt = con.prepareStatement(updateSQL);  // Use updateSQL instead of sql

            update_stmt.setFloat(1, m.getPrice());                                     // Set Selling_price
            update_stmt.setFloat(2, m.getQuantity() + rowsSelected.getFloat("Available_Quantity")); // Set Available_Quantity
            update_stmt.setFloat(3, m.getUnitCost());                                   // Set unit_cost
            update_stmt.setString(4, m.getName());                                      // Set Name
            update_stmt.setString(5, m.getExpiry());                                    // Set Expiry


            int rowsAffected = update_stmt.executeUpdate();

        } else {
            System.out.println("No med with same name and expiry exists");
            //proceed to add a new medicine to inventory;
            String serial_id = createUniqueID();

            String sql_insert = "INSERT INTO `shop_inventory` (`serial_id`, `Name`, `Dose`, `Selling_price`, `Expiry`, `Type`, `Available_Quantity`, `unit_cost`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


            PreparedStatement statement = con.prepareStatement(sql_insert);

// Set the parameter values
            statement.setString(1, serial_id);
            statement.setString(2, m.getName());
            statement.setString(3, m.getDose());
            statement.setFloat(4, m.getPrice());
            statement.setString(5, m.getExpiry()); // Assuming the expiry date is already a String
            statement.setString(6, m.getType());
            statement.setFloat(7, m.getQuantity());
            statement.setFloat(8, m.getUnitCost());

// Execute the insert
            statement.executeUpdate();

            System.out.println("Record inserted successfully!");



        }

        ShopMenu.getInstance().refreshList();

    }


}
