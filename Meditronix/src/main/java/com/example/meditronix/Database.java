package com.example.meditronix;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public Connection dbConnect()  {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://127.0.0.1:3306/mylocaldb";
            String username = "root";
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
    public void createPrescriptionTable(String prescriptionCode, String patientName, LocalDateTime dateTime, String age, String gender) {
        try {
            Connection con = dbConnect();
            Statement stmt = con.createStatement();

            System.out.println("Prescription table created: " + prescriptionCode);
            System.out.println("passed patient name in creatprestablefunction: " + patientName);

            // Create a new table for the prescription
            String sql = "CREATE TABLE IF NOT EXISTS " + prescriptionCode + " (" +
                    "medicine_name VARCHAR(255), " +
                    "dosage VARCHAR(50), " +
                    "quantity INT, " +
                    "frequency VARCHAR(50), " +
                    "generated_date DATE, " +
                    "generated_time TIME)";
            stmt.executeUpdate(sql);



            // Insert patient data into the 'patients' table
            insertPatientDataIntoPatients(prescriptionCode, patientName, dateTime, age, gender);

            // Close resources
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //to insert patients data into patients table
    public void insertPatientDataIntoPatients(String prescriptionCode, String patientName, LocalDateTime dateTime, String age, String gender) {
        try {
            Connection con = dbConnect();
            System.out.println("Passed patient name in insertPatientDataIntoPatients function: " + patientName);

            // Insert patient data into the 'patients' table with date, time, age, and gender
            String sql = "INSERT INTO patients (uniqueid, name, age, gender, generated_date, generated_time) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, prescriptionCode);
            stmt.setString(2, patientName);
            stmt.setString(3, age); // Set the age value
            stmt.setString(4, gender); // Set the gender value
            stmt.setDate(5, java.sql.Date.valueOf(dateTime.toLocalDate()));
            stmt.setTime(6, java.sql.Time.valueOf(dateTime.toLocalTime()));
            stmt.executeUpdate();

            System.out.println("Patient data inserted into 'patients' table: " + patientName);

            // Close resources
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //to insert medicine data into the prescription table
    public void insertMedicineData(String prescriptionCode, String medicineName, String dosage, int quantity, String frequency, LocalDateTime dateTime) {
        try {
            Connection con = dbConnect();

            // Insert medicine data into the prescription table with date and time
            String sql = "INSERT INTO " + prescriptionCode + " (medicine_name, dosage, quantity, frequency, generated_date, generated_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, medicineName);
            pstmt.setString(2, dosage);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, frequency);
            pstmt.setDate(5, java.sql.Date.valueOf(dateTime.toLocalDate())); // Convert LocalDateTime to Date
            pstmt.setTime(6, java.sql.Time.valueOf(dateTime.toLocalTime())); // Convert LocalDateTime to Time
            pstmt.executeUpdate();

            System.out.println("Medicine data inserted into prescription table: " + prescriptionCode);

            // Close resources
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //matched named patients unique id is retrived to show on to the tableview of view prescription
    public List<Prescription> getPrescriptionCodesByPatientName(String patientName) {
        List<Prescription> prescriptions = new ArrayList<>();
        try (Connection conn = dbConnect();
             PreparedStatement stmt = conn.prepareStatement("SELECT LOWER(uniqueid) AS uniqueid, generated_date, generated_time FROM patients WHERE name = ?")) {
            stmt.setString(1, patientName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String uniqueId = rs.getString("uniqueid");
                Date generatedDate = rs.getDate("generated_date");
                Time generatedTime = rs.getTime("generated_time");
                prescriptions.add(new Prescription(uniqueId, generatedDate, generatedTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }


    //to retrieve patient name age gender for prescription
    public PatientDataPrescription getPatientData(String prescriptionCode) {
        try (Connection conn = dbConnect();
             PreparedStatement stmt = conn.prepareStatement("SELECT name, age, gender FROM patients WHERE uniqueid = ?")) {
            stmt.setString(1, prescriptionCode.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String age = rs.getString("age");
                String gender = rs.getString("gender");
                return new PatientDataPrescription(name, age, gender);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //to retrive medicine data for prescription
    public ObservableList<MedicineDataPrescription> getMedicineData(String prescriptionCode) {
        ObservableList<MedicineDataPrescription> medicineDataList = FXCollections.observableArrayList();
        try (Connection conn = dbConnect();
             PreparedStatement stmt = conn.prepareStatement("SELECT medicine_name, dosage, quantity, frequency FROM " + prescriptionCode)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String medicineName = rs.getString("medicine_name");
                String dosage = rs.getString("dosage");
                int quantity = rs.getInt("quantity");
                String frequency = rs.getString("frequency");
                medicineDataList.add(new MedicineDataPrescription(medicineName, dosage, quantity, frequency));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicineDataList;
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

    public List<String> getPatientNameSuggestions(String nameSubstring) {
        List<String> suggestions = new ArrayList<>();
        try {
            String query = "SELECT name FROM patients WHERE name LIKE ?";
            Connection conn = dbConnect();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, "%" + nameSubstring + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                suggestions.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suggestions;
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

    public boolean addMemo() throws SQLException {
        Connection con = null;
        Statement stmt = null;

        try {
            con = dbConnect();
            stmt = con.createStatement();

            // Insert a new row into the 'memos' table
            String insertSql = "INSERT INTO memos () VALUES ()";
            int rowsAffected = stmt.executeUpdate(insertSql, Statement.RETURN_GENERATED_KEYS);

            return rowsAffected > 0; // Return true if insert was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        } finally {
            // Close resources
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }
    }


    public int getLastMemoValue() throws SQLException {
        Connection con = dbConnect();
        Statement stmt = con.createStatement();

        String sql = "SELECT No FROM memos ORDER BY No DESC LIMIT 1";
        ResultSet rs = stmt.executeQuery(sql);

        int lastValue = -1;
        if (rs.next()) {
            lastValue = rs.getInt("No");
        }

        // Close resources
        rs.close();
        stmt.close();
        con.close();

        return lastValue;
    }

    public float getMedicineQuantity(String tableName, String medicineName, String dosage) throws SQLException {
        String sql = "SELECT Available_Quantity FROM " + tableName + " WHERE Name = ? AND Dose = ?";
        try (Connection con = dbConnect(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, medicineName);
            pstmt.setString(2, dosage);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("Available_Quantity");
                } else {
                    throw new SQLException("No record found for the specified medicine and dosage.");
                }
            }
        }
    }

    public boolean removeMedFromCart(String tableName, String medicineName, String dosage, float addedQuantity) throws SQLException {
        String updateTableSql = "UPDATE " + tableName + " SET quantity = quantity + ? WHERE medicine_name = ? AND dosage = ?";
        String updateInventorySql = "UPDATE shop_inventory SET Available_Quantity = Available_Quantity + ? WHERE Name = ? AND Dose = ?";

        try (Connection con = dbConnect();
             PreparedStatement pstmt1 = con.prepareStatement(updateTableSql);
             PreparedStatement pstmt2 = con.prepareStatement(updateInventorySql)) {

            // Update quantity in the specified table
            pstmt1.setFloat(1, addedQuantity);
            pstmt1.setString(2, medicineName);
            pstmt1.setString(3, dosage);
            int rowsUpdated1 = pstmt1.executeUpdate();

            // Update quantity in the shop_inventory table
            pstmt2.setFloat(1, addedQuantity);
            pstmt2.setString(2, medicineName);
            pstmt2.setString(3, dosage);
            int rowsUpdated2 = pstmt2.executeUpdate();

            // Check if both updates were successful
            return rowsUpdated1 > 0 && rowsUpdated2 > 0;
        }
    }

    public float updateMedicineQuantity(String tableName, String medicineName, String dosage, float addedQuantity) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        float price = 0;

        try {
            con = dbConnect();
            con.setAutoCommit(false); // Start transaction

            // Update quantity in the specified table
            String updateTableSql = "UPDATE " + tableName + " SET quantity = quantity - ? WHERE medicine_name = ? AND dosage = ?";
            pstmt = con.prepareStatement(updateTableSql);
            pstmt.setFloat(1, addedQuantity);
            pstmt.setString(2, medicineName);
            pstmt.setString(3, dosage);
            pstmt.executeUpdate();

            // Update quantity in the shop_inventory table and get the price
            String updateInventorySql = "UPDATE shop_inventory SET Available_Quantity = Available_Quantity - ? WHERE Name = ? AND Dose = ?";
            pstmt = con.prepareStatement(updateInventorySql);
            pstmt.setFloat(1, addedQuantity);
            pstmt.setString(2, medicineName);
            pstmt.setString(3, dosage);
            pstmt.executeUpdate();

            String getPriceSql = "SELECT Selling_price FROM shop_inventory WHERE Name = ? AND Dose = ?";
            pstmt = con.prepareStatement(getPriceSql);
            pstmt.setString(1, medicineName);
            pstmt.setString(2, dosage);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                price = rs.getFloat("Selling_price");
            } else {
                throw new SQLException("No record found for the specified medicine and dosage in the shop inventory.");
            }

            con.commit(); // Commit transaction
        } catch (SQLException e) {
            if (con != null) {
                con.rollback(); // Rollback transaction on error
            }
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (con != null) con.close();
        }

        return price;
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

    public static boolean isMedicineExpired(String medicineName, String dose,Connection con) {
        String sql = "SELECT Expiry FROM shop_inventory WHERE Name = ? AND Dose = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, medicineName);
            pstmt.setString(2, dose);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String expiryDateStr = rs.getString("Expiry");
                    if (expiryDateStr != null) {
                        // Define the date format used in the Expiry field
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                        // Parse the expiry date string into a LocalDate object
                        LocalDate expiryDate = LocalDate.parse(expiryDateStr, formatter);

                        // Get the current date
                        LocalDate currentDate = LocalDate.now();

                        // Compare the expiry date with the current date
                        return expiryDate.isBefore(currentDate);
                    } else {
                        // Handle the case where the expiry date is null
                        return false;
                    }
                } else {
                    // No record found for the specified medicine name and dose
                    return false;
                }
            }
        } catch (SQLException | DateTimeParseException e) {
            // Handle potential SQL and date parsing errors here
            e.printStackTrace();
            return false;
        }
    }


    //for dosage suggestion
    public List<String> getDosagesByMedicineName(String medicineName) {
        List<String> dosages = new ArrayList<>();
        String query = "SELECT DISTINCT Dose FROM shop_inventory WHERE Name = ?";
        try (Connection con = dbConnect();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, medicineName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                dosages.add(rs.getString("Dose"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dosages;
    }





}
