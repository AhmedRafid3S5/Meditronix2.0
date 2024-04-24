package com.example.meditronix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddMedicinePanel extends Pane implements Initializable {
    public Button addButton;

    @FXML
    private TextField BuyCostField;

    @FXML
    private DatePicker expiryDateField;

    @FXML
    private TextField QuantityField;

    @FXML
    private TextField SellCostField;

    @FXML
    private ChoiceBox<String> typeList;

    @FXML
    private HBox addHboxPanel;

    @FXML
    private AnchorPane addPanel;

    @FXML
    private TextField doseField;

    @FXML
    private TextField nameField;

    @FXML
    private Label warningLabel;
    private Stage stage;

    private String selectedType;

    private Connection con;
    private Database localDB;

    private final String[] types = {"Prescription","Generic"};

    public void show() {
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
          typeList.getItems().addAll(types);
          typeList.setOnAction(this::returnType);
          localDB = new Database();
          con = localDB.dbConnect();


          BuyCostField.setText("0.0");
          doseField.setText("-");

    }

    public void returnType(ActionEvent event)
    {
        selectedType = typeList.getValue();
    }

    @FXML
    public void addToInventory(ActionEvent event) throws SQLException {




           Float buyingCost, sellingCost, quantityAdded;
           String name, type, dose;
           LocalDate expiryDate;
           String date;

           //Set with default values if field is not completed


           // expiryDate = LocalDate.parse("31/12/2100");

           //medicine class takes in expiry date as a string, so format LocalDate to a string
           if (expiryDateField.getValue() != null) {
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
               date = expiryDateField.getValue().format(formatter);
           } else {
               expiryDateField.setValue(LocalDate.of(2100, 12, 31));
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
               date = expiryDateField.getValue().format(formatter);
           }
           if (BuyCostField.getText() != null) {
               buyingCost = Float.valueOf(BuyCostField.getText());
           } else {
               BuyCostField.setText("0.0");
               buyingCost = Float.valueOf(BuyCostField.getText());
           }
           if (doseField.getText() != null) {
               dose = doseField.getText();
           } else {
               doseField.setText("_");
               dose = doseField.getText();
           }

           if (nameField.getText() != null && typeList.getValue() != null &&
                   SellCostField.getText() != null && QuantityField.getText() != null) {
               //Must fill up these fields
               name = nameField.getText();
               type = typeList.getValue();
               sellingCost = Float.valueOf(SellCostField.getText());
               quantityAdded = Float.valueOf(QuantityField.getText());

               Medicine medicine = new Medicine(name, dose, date, type, sellingCost, quantityAdded, buyingCost);


               localDB.addMedicine(medicine, con);
           } else {
               warningLabel.setText("Fields with * must be filled!");
           }




    }




}
