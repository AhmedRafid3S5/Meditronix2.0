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
    private TextField SignUpName;

    @FXML
    private TextField SignUpPassword;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SignUpName.setStyle("-fx-text-fill: #0e0707;");
    }

    @FXML
    void CreateAccountPress(ActionEvent event) {
        String username = SignUpName.getText();
        String password = SignUpPassword.getText();

        Database database = new Database();
        Connection con = database.dbConnect();

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
                    System.out.println("User inserted successfully!");
                    // Show a popup window indicating successful user insertion
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("User inserted successfully!");

                    // Get the stage of the popup window and wait for user confirmation
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alert.showAndWait();

                    // Switch to MainMenu.fxml scene upon user confirmation
                    if (alert.getResult() == ButtonType.OK) {
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


}
