package com.example.meditronix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class stockSettingController implements Initializable {
    @FXML
    private TextField lowStockInputField;

    @FXML
    private Button setValueButton;

    private boolean isValidLimit(String value) {
        return value.matches("\\d+") && Integer.parseInt(value) >= 0;
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void onSetButtonClick(ActionEvent event) throws SQLException {
        int upperBound = 0;
        if(isValidLimit(lowStockInputField.getText())){
            upperBound = Integer.parseInt(lowStockInputField.getText());
        }
        else
        {
            showErrorAlert("Limit has invalid characters or limit is negative");
        }
        ShopMenu.getInstance().setLowStockLimit(upperBound);
        ShopMenu.getInstance().refreshList();
        Connection con = ShopMenu.getInstance().getConnection();
        ShopMenu.getInstance().getGlobalDB().setLowStockValue(con,upperBound);
    }

    @FXML
    void onLowStockSettingClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("stockDialogue.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 760, 510);

        //For dev, use this entry point
        Scene scene = new Scene(fxmlLoader.load(), 275,173);

        InputStream inputStream = getClass().getResourceAsStream("images/settingsIcon.png");
        Image image = new Image(inputStream);
        Stage stage = new Stage();
        stage.getIcons().add(image);
        stage.setTitle("Warning settings");
        stage.setScene(scene);
        stage.show();


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lowStockInputField.setText(String.valueOf(ShopMenu.getInstance().getLowStockLimit()));
    }
}
