package com.example.meditronix;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class GenericPurchaseController implements Initializable {

    //Inventory Column
    @FXML
    private TableView<Medicine> GenericTable;
    @FXML
    private TableColumn<Medicine, String> DosageColumn;

    @FXML
    private TableColumn<Medicine, String> NameColumn;

    @FXML
    private TableColumn<Medicine, Float> PriceColumn;

    @FXML
    private TableColumn<Medicine, Float> QuantityColumn;

    ObservableList<Medicine> list = FXCollections.observableArrayList();


    // Codes for the Cart Table
    @FXML
    private TableView<Medicine> CartTable;
    @FXML
    private TableColumn<Medicine, String> CartNameColumn;
    @FXML
    private TableColumn<Medicine, String> CartDosageColumn;
    @FXML
    private TableColumn<Medicine, Float> CartQuantityColumn;
    @FXML
    private TableColumn<Medicine, Float> CartPriceColumn;

    @FXML
    private ComboBox<Integer> QuantityBox;
    @FXML
    private Button AddToCart;
    @FXML
    private Button deleteButton;

    @FXML
    private Label subtotalLabel;

    ObservableList<Medicine> cartList = FXCollections.observableArrayList();
    private Database GlobalDB;
    private Connection GlobalConnect;
    private ResultSet rs;

    @FXML
    private Button BackButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        GlobalDB = new Database();
        GlobalConnect = GlobalDB.dbConnect();
        NameColumn.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Name"));
        DosageColumn.setCellValueFactory(new PropertyValueFactory<Medicine,String>("Dose"));
        QuantityColumn.setCellValueFactory(new PropertyValueFactory<Medicine,Float>("Quantity"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<Medicine,Float>("Price"));


        CartNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        CartDosageColumn.setCellValueFactory(new PropertyValueFactory<>("Dose"));
        CartQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        CartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        try {
            rs = new Database().showGeneric();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            while (rs.next()) {
                // Create a new Medicine object from each row in the ResultSet
                Medicine medicine = new Medicine(rs);
                // Add the medicine object to the ObservableList
                list.add(medicine);
            }
        } catch (SQLException e) {
            // Handle potential SQLException here
            e.printStackTrace();
        }
        GenericTable.setItems(list);

        CartTable.setItems(cartList);
        for (int i = 1; i <= 15; i++) {
            QuantityBox.getItems().add(i);
        }
        QuantityBox.setValue(0);
        //  QuantityBox.getSelectionModel().selectFirst();
    }

    @FXML
    void BackButtonPressed(ActionEvent event) throws IOException {
        Object root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("PurchaseTypeSelection.fxml")));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void AddToCartButtonPressed(ActionEvent event) {
        Medicine selectedMedicine = GenericTable.getSelectionModel().getSelectedItem();
        Integer quantityValue = QuantityBox.getValue();

        if (quantityValue == null || quantityValue == 0) {
            showAlert("No Quantity", "No quantity selected. Please enter a quantity.");
            return;
        }

        Float selectedQuantity = quantityValue.floatValue();

        if (selectedMedicine == null) {
            showAlert("No Selection", "No medicine selected. Please select a medicine.");
        } else {
            if (selectedMedicine.getQuantity() >= selectedQuantity) {
                // Calculate the new quantity
                Float newQuantity = selectedMedicine.getQuantity() - selectedQuantity;

                // Update the database
                try {
                    Medicine updatedMedicine = selectedMedicine;
                    updatedMedicine.setQuantity(newQuantity);
                    if (GlobalDB.updateMedicine(selectedMedicine,updatedMedicine, GlobalConnect)) {
                        // Update the in-memory table
                        GenericTable.refresh();

                        // Add to the CartTable
                        Medicine medicineInCart = new Medicine(selectedMedicine.getName(), selectedMedicine.getDose(), selectedQuantity, selectedMedicine.getPrice());
                        cartList.add(medicineInCart);
                        CartTable.refresh();
                    } else {
                        showAlert("Database Update Error", "Failed to update the database.");
                    }
                } catch (SQLException e) {
                    showAlert("Database Update Error", "An error occurred while updating the database.");
                }
            } else {
                // Handle case when selected quantity is more than available quantity
                showAlert("Insufficient Stock", "Selected quantity exceeds available stock.");
            }
        }
        calculateAndSetSubtotal();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void DeleteButtonPressed(ActionEvent event) {
        Medicine medInCart = CartTable.getSelectionModel().getSelectedItem();

        if (medInCart == null) {
            showAlert("No Selection", "No medicine selected in the cart. Please select a medicine to remove.");
            return;
        }

        try {
            // Retrieve the ResultSet containing all medicines from the database
            ResultSet rs;
            try {
                rs = new Database().showGeneric();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to retrieve data from the database", e);
            }
            list.clear();
            // Find the corresponding medicine in the ResultSet
            while (rs.next()) {
                Medicine medicine = new Medicine(rs);
                if (medicine.getName().equals(medInCart.getName()) &&
                        medicine.getDose().equals(medInCart.getDose()) &&
                        medicine.getPrice().equals(medInCart.getPrice())) {
                    // Update the corresponding entry in the database

                    medicine.setQuantity(medInCart.getQuantity()+ medicine.getQuantity());
                    if (GlobalDB.updateMedicine(medicine, medicine, GlobalConnect)) {

                    } else {
                        showAlert("Database Update Error", "Failed to update the database.");
                    }
                    list.add(medicine);
                }
                else
                {
                    list.add(new Medicine(rs));
                }
            }


            GenericTable.setItems(list);
            GenericTable.refresh();
            cartList.remove(medInCart);
            CartTable.refresh();

        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while accessing the database: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
        calculateAndSetSubtotal();

    }

    private void calculateAndSetSubtotal() {
        float subtotal = 0.0f;
        for (Medicine medicine : cartList) {
            subtotal += medicine.getQuantity() * medicine.getPrice();
        }
        // Update the subtotal label with the calculated subtotal
        subtotalLabel.setText(String.format("Subtotal:        %.2f   Tk", subtotal));
    }
}
