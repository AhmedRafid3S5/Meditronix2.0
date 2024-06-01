package com.example.meditronix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class ChangeCredentialsController {
    @FXML
    private Button confirmButton;

    @FXML
    private TextField newPassField;

    @FXML
    private TextField newUsernameField;

    @FXML
    private TextField currentUserNameField;

    @FXML
    private TextField currentPassword;

    @FXML
    public  void onConfirmClicked(ActionEvent event) throws SQLException {

        if(newPassField.getText() != null && newUsernameField.getText() != null &&
                currentUserNameField.getText() != null && currentPassword.getText() != null)
        {

            String newPass = newPassField.getText();
            String newName = newUsernameField.getText();
            String currentName = currentUserNameField.getText();
            String currentPasscode = currentPassword.getText();

            Connection con = ShopMenu.getInstance().getConnection();
            System.out.println(currentName + currentPasscode);

            if(ShopMenu.getInstance().getGlobalDB().changeCredentials(con,currentName,currentPasscode,newName,newPass))
            {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("New credentials accepted");
                alert.setTitle("Account info");
                alert.setContentText("Change successful");
                alert.showAndWait();
            }

            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setTitle("Account info");
                alert.setContentText("Invalid changes or current username or passwaord invalid");
                alert.showAndWait();
            }


        }
    }


    @FXML
    void onChangeCredentialsClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("changeCredentialsWindow.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 760, 510);

        //For dev, use this entry point
        Scene scene = new Scene(fxmlLoader.load(), 530,400);

        InputStream inputStream = getClass().getResourceAsStream("images/settingsIcon.png");
        Image image = new Image(inputStream);
        Stage stage = new Stage();
        stage.getIcons().add(image);
        stage.setTitle("Account settings");
        stage.setScene(scene);
        stage.show();


    }
}
