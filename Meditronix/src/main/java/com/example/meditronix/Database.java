package com.example.meditronix;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Database {

     public Connection dbConnect()  {
         try {
             //Updated driver class--------------------------------------------------
             Class.forName("com.mysql.cj.jdbc.Driver");


             //-------------Local connection configuration---------------------------
             /*String url = "jdbc:mysql://127.0.0.1:3306/mylocaldb";
             String username = "root";
             String password = "admin1234";*/
             //----------------------------------------------------------------------

             //------------Aiven MySQL connection configuration----------------------
             String host = "meditronix-ahmedrafidx360-5ecd.f.aivencloud.com";
             String port = "22467";
             String databaseName = "meditronix";
             String userName = "avnadmin";
             String password = "AVNS_8kqCs13cKGsjHicTaW5";
             Connection con =DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?sslmode=require", userName, password);
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

    public ResultSet searchByName(Connection con, String name) throws SQLException {
        String sql = "SELECT * FROM shop_inventory WHERE Name LIKE ?;";
        PreparedStatement pStmt = con.prepareStatement(sql);

        // Include the '%' wildcards directly in the parameter based on name passed
        char firstChar = name.charAt(0);

        if(Character.isUpperCase(firstChar)) {
            pStmt.setString(1,  name + "%");
        }
        else {
            pStmt.setString(1,  "%" + name + "%");
        }

        ResultSet rs = pStmt.executeQuery();
        return rs;
    }

    public ResultSet searchByDate(Connection con, String date) throws SQLException {
        String sql = "SELECT * FROM shop_inventory WHERE serial_id LIKE ?;";
        PreparedStatement pStmt = con.prepareStatement(sql);
        pStmt.setString(1,date+"%");

        ResultSet rs = pStmt.executeQuery();
        return rs;
    }

    public ResultSet searchByNameDose(Connection con, String name,String dose) throws SQLException {
        String sql = "SELECT * FROM shop_inventory WHERE Name LIKE ? AND dose Like ?;";
        PreparedStatement pStmt = con.prepareStatement(sql);
        char firstChar = name.charAt(0);

        if(Character.isUpperCase(firstChar)) {
            pStmt.setString(1,  name + "%");
        }
        else {
            pStmt.setString(1,  "%" + name + "%");
        }

        pStmt.setString(2,dose);

        ResultSet rs = pStmt.executeQuery();
        return rs;
    }

    public ResultSet strictSearch(Connection con, String name,String dose,String date) throws SQLException {
        String sql = "SELECT * FROM shop_inventory WHERE Name LIKE ? AND dose Like ? AND serial_id LIKE ?;";
        PreparedStatement pStmt = con.prepareStatement(sql);
        char firstChar = name.charAt(0);

        if(Character.isUpperCase(firstChar)) {
            pStmt.setString(1,  name + "%");
        }
        else {
            pStmt.setString(1,  "%" + name + "%");
        }

        pStmt.setString(2,dose);

        pStmt.setString(3,date+"%");

        ResultSet rs = pStmt.executeQuery();
        return rs;
    }


    public int fetchLowStockValue(Connection con) throws SQLException{
         Statement stmt = con.createStatement();
         String sql = "SELECT lowStockValue FROM stock_parameters;";
         ResultSet rs = stmt.executeQuery(sql);

        if(rs.next()) {
             return rs.getInt("lowStockValue");
         }
        else
            return 1; //default value
    }

    public void setLowStockValue(Connection con, int newLowStockValue) throws SQLException {
        String sql = "UPDATE stock_parameters " +
                "SET lowStockValue = ? " +
                "WHERE id = 0; ";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, newLowStockValue);

        stmt.executeUpdate();
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

    public String currentDate()
    {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentTime.format(formatter);
    }

    public void addMedicine(Medicine m, Connection con, Label tellStatus) throws SQLException{

         String added = null;


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
            if(rowsAffected >0)
            {
                added = "Medicine was added to an existing record with same name & expiry";
            }

        } else {

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

            added = "New medicine added to inventory";


        }

        tellStatus.setText(added);
        tellStatus.setWrapText(true);
        tellStatus.setVisible(true);

        // Create a Timeline to hide the label after 3 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.5), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Hide the warning label after the specified duration
                        tellStatus.setVisible(false);
                    }
                })
        );

        // Play the timeline once to hide the label after 3 seconds
        timeline.setCycleCount(1);
        timeline.play();

        ShopMenu.getInstance().refreshList();

    }

    //to create a new table for a prescription
    public void createPrescriptionTable(String prescriptionCode) {
        try {
            Connection con = dbConnect();
            Statement stmt = con.createStatement();

            // Create a new table for the prescription
            String sql = "CREATE TABLE IF NOT EXISTS " + prescriptionCode + " (" +
                    "medicine_name VARCHAR(255), " +
                    "dosage VARCHAR(50), " +
                    "quantity INT, " +
                    "frequency VARCHAR(50))";
            stmt.executeUpdate(sql);

            System.out.println("Prescription table created: " + prescriptionCode);

            // Close resources
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //to insert medicine data into the prescription table
    public void insertMedicineData(String prescriptionCode, String medicineName, String dosage, int quantity, String frequency) {
        try {
            Connection con = dbConnect();

            // Insert medicine data into the prescription table
            String sql = "INSERT INTO " + prescriptionCode + " (medicine_name, dosage, quantity, frequency) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, medicineName);
            pstmt.setString(2, dosage);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, frequency);
            pstmt.executeUpdate();

            System.out.println("Medicine data inserted into prescription table: " + prescriptionCode);

            // Close resources
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // to delete medicine data from the database
    public void deleteMedicineData(String prescriptionCode, String medicineName, String dosage, String quantity, String frequency) {
        String sql = "DELETE FROM " + prescriptionCode + " WHERE medicine_name = ? AND dosage = ? AND quantity = ? AND frequency = ?";
        try (Connection conn = dbConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, medicineName);
            stmt.setString(2, dosage);
            stmt.setString(3, quantity);
            stmt.setString(4, frequency);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Medicine data deleted successfully from the database.");
            } else {
                System.out.println("No medicine data found for deletion.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting medicine data from the database: " + e.getMessage());
        }
    }



    // to delete patient data from the database
    public void deletePatientData(String patientName) {
        String sql = "DELETE FROM PatientTable WHERE Name = ?";
        try (Connection conn = dbConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientName);
            stmt.executeUpdate();
            System.out.println("Patient data deleted successfully from the database.");
        } catch (SQLException e) {
            System.out.println("Error deleting patient data from the database: " + e.getMessage());
        }
    }




    //Codes for the Medicine Purchase Menu
    public ResultSet showGeneric() throws SQLException {

        Connection con = dbConnect();
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM shop_inventory WHERE Type = 'Generic'; ";
        ResultSet rs = stmt.executeQuery(sql);
        return rs;

    }


    //SQL function to update a selected med in inventory
    public boolean updateMedicine(Medicine old_med,Medicine new_med,Connection con) throws SQLException {
        String updateSQL = "UPDATE `shop_inventory`\n" +
                "SET\n" +
                "  `Name` = ?,\n" +
                "  `Selling_price` = ?,\n" +
                "  `Available_Quantity` = ?,\n" +
                "  `unit_cost` = ?,\n" +
                "  `Dose` = ?,\n" +
                "  `Expiry` = ?,\n" +
                "  `Type` = ?\n" +
                "WHERE `serial_id` = ?;";

        PreparedStatement update_stmt = con.prepareStatement(updateSQL);  // Use updateSQL instead of sql

        update_stmt.setString(1, new_med.getName());
        update_stmt.setFloat(2,new_med.getPrice());
        update_stmt.setFloat(3,new_med.getQuantity());
        update_stmt.setFloat(4,new_med.getUnitCost());
        update_stmt.setString(5, new_med.getDose());
        update_stmt.setString(6, new_med.getExpiry());
        update_stmt.setString(7, new_med.getType());
        update_stmt.setString(8, old_med.getSerial_id());


        int rowsAffected = update_stmt.executeUpdate();

        if(rowsAffected > 0)
        {

            return true;
        }

        return false;

    }

    //SQL function to change password and username Date 2nd June
    public boolean changeCredentials(Connection con, String currentUsername,String currentPassword,
                                     String newUsername, String newPassword) throws SQLException {

        String returned_username = null;
        String returned_password = null;

        String sql = "Select * From users where username = ? ";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,currentUsername);

        ResultSet rs = stmt.executeQuery();

        if(rs.next()){
            returned_username = rs.getString("username");
            returned_password = rs.getString("password");
            
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setTitle("Account info");
            alert.setContentText("Invalid changes or current username or passwaord invalid");
            alert.showAndWait();
            
        }

        if(returned_username.equals(currentUsername) && returned_password.equals(currentPassword)) {
            String sql2 = "UPDATE users " +
                    "SET username = ?, " +
                    "password = ? " +
                    "WHERE username = ?";

            PreparedStatement update_stms = con.prepareStatement(sql2);

            update_stms.setString(1, newUsername);
            update_stms.setString(2, newPassword);
            update_stms.setString(3, currentUsername);

            int update = update_stms.executeUpdate();

            if (update > 0)
                return true;
            else
                return false;
        }

        return false;


    }


}
