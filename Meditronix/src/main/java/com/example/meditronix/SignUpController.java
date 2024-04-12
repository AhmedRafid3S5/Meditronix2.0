package com.example.meditronix;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.*;
import java.util.EventObject;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private Button CreateAccount;

    @FXML
    private Button ReturnToMain;

    @FXML
    private TextField SignUpName;

    @FXML
    private TextField SignUpPassword;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SignUpName.setStyle("-fx-text-fill: #0e0707;");
    }

    @FXML
    void CreateAccountPress(ActionEvent event) throws IOException {
        String username = SignUpName.getText();
        String password = SignUpPassword.getText();
        Database database = new Database();
        Connection con = database.dbConnect();

        // Check if username and password are not empty
        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please enter a valid username and password.");
            alert.showAndWait();
            return; // Return from the method if username or password is empty
        }

        if (con != null) {
            try {
                // Prepare the INSERT statement
                String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'customer')";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);

                // Execute the INSERT statement
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    // Show a popup window indicating successful user insertion
                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("User inserted successfully!");
                    successAlert.showAndWait();

                    // Switch to MainScreen.fxml scene upon user confirmation
                    if (successAlert.getResult() == ButtonType.OK) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        primaryStage.setScene(scene);
                        primaryStage.show();
                    }
                } else {
                    System.out.println("Failed to insert user!");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting user: " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    // Close the connection
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Failed to connect to the database!");
        }
    }
    @FXML
    void ReturnPress(ActionEvent event) throws IOException{
        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();
        Database db = new Database();
        db.dbConnect();
    }



}
