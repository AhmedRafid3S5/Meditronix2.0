package com.example.meditronix;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        localDB = ShopMenu.getInstance().getGlobalDB();
        con = ShopMenu.getInstance().getConnection();

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
    public void updateMedicine(ActionEvent actionEvent) throws SQLException {

        String updateStatusLabel= null;

        Float buyingCost, sellingCost, quantityAdded;
        String name, type, dose;
        LocalDate expiryDate;
        String date;
        String serial_id;

        Medicine old_medicine = ShopMenu.getInstance().getInventoryTable().getSelectionModel().getSelectedItem();
        Medicine new_medicine;


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = updateExpiryDateField.getValue().format(formatter);
            buyingCost = Float.valueOf(updateBuyCostField.getText());
            sellingCost = Float.valueOf(updateSellCostField.getText());
            dose = updateDoseField.getText();
            quantityAdded = Float.valueOf(updateQuantityField.getText());
            name = updateNameField.getText();
            type = updateTypeList.getValue();

            new_medicine = new Medicine(name, dose, date, type, sellingCost, quantityAdded, buyingCost);

            if (localDB.updateMedicine(old_medicine, new_medicine, con)) {
               updateStatusLabel = "Medicine updated successfully!";
            } else {
                updateStatusLabel = "Unable to update selected medicince";
            }

            ShopMenu.getInstance().refreshList();

        newWarningLabel.setText(updateStatusLabel);

        newWarningLabel.setVisible(true);

        // Create a Timeline to hide the label after 3 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Hide the warning label after the specified duration
                        newWarningLabel.setVisible(false);
                    }
                })
        );

        // Play the timeline once to hide the label after 3 seconds
        timeline.setCycleCount(1);
        timeline.play();



    }

    @FXML
    public void showSelectedMed(ActionEvent event)
    {
        if(selectByIndexField.getText() != null) {
            int index = Integer.valueOf(selectByIndexField.getText());
        }

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
