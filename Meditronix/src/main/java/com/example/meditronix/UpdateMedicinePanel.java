package com.example.meditronix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateMedicinePanel implements Initializable {

    @FXML
    private Label newWarningLabel;

    @FXML
    private Button updateButton;

    @FXML
    private TextField updateBuyCostField;

    @FXML
    private TextField updateDoseField;

    @FXML
    private DatePicker updateExpiryDateField;

    @FXML
    private TextField updateNameField;

    @FXML
    private AnchorPane updatePanel;

    @FXML
    private TextField updateQuantityField;

    @FXML
    private TextField updateSellCostField;

    @FXML
    private ChoiceBox<String> updateTypeList;

    @FXML
    private Button searchByIndexBtn;

    @FXML
    private TextField selectByIndexField;

    private final String[] types = {"Prescription","Generic"};
    
    Connection con ;
    Database localDB;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTypeList.getItems().addAll(types);
        updateTypeList.setOnAction(this::returnType);
        localDB = new Database();
        con = localDB.dbConnect();

        //Fetch selected medicine
        Medicine medicine = ShopMenu.getInstance().getInventoryTable().getSelectionModel().getSelectedItem();
        updateBuyCostField.setText(String.valueOf(medicine.getUnitCost()));
        updateDoseField.setText(medicine.getDose());
        updateNameField.setText(medicine.getName());
        updateSellCostField.setText(String.valueOf(medicine.getPrice()));
        updateTypeList.setValue(medicine.getType());
        updateExpiryDateField.setValue(LocalDate.parse(medicine.getExpiry()));
        updateQuantityField.setText(String.valueOf(medicine.getQuantity()));



    }

    public void returnType(ActionEvent event)
    {
        String selectedType = updateTypeList.getValue();
    }
    public void updateMedicine(ActionEvent actionEvent) {

    }

    @FXML
    public void showSelectedMed(ActionEvent event)
    {
        int index = Integer.valueOf(selectByIndexField.getText());


        Medicine medicine = ShopMenu.getInstance().getInventoryTable().getSelectionModel().getSelectedItem();
        updateBuyCostField.setText(String.valueOf(medicine.getUnitCost()));
        updateDoseField.setText(medicine.getDose());
        updateNameField.setText(medicine.getName());
        updateSellCostField.setText(String.valueOf(medicine.getPrice()));
        updateTypeList.setValue(medicine.getType());
        updateExpiryDateField.setValue(LocalDate.parse(medicine.getExpiry()));
        updateQuantityField.setText(String.valueOf(medicine.getQuantity()));

    }
}
