package com.example.meditronix;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.List;

public class ViewPrescriptionController {

    private Scene scene;
    private Stage stage;
    @FXML
    private Button backButton;

    @FXML
    private TextField searchpatientnametf;

    @FXML
    private TableView<Prescription> prescodetable;

    @FXML
    private TableColumn<Prescription, String> presCodeColumn;
    @FXML
    private TableColumn<Prescription, Time> timeColumn;
    @FXML
    private TableColumn<Prescription, Date> dateColumn;

    @FXML
    private Button searchpatientbutton;
    private Database database = new Database();

    @FXML
    void backButtonPressed(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DoctorMenu.fxml"));
            Parent root = fxmlLoader.load();
            scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchbuttonpressed(ActionEvent event) {
        String patientName = searchpatientnametf.getText();
        System.out.println("Name: " + patientName);
        if (!patientName.isEmpty()) {
            List<Prescription> prescriptions = database.getPrescriptionCodesByPatientName(patientName);
            if (!prescriptions.isEmpty()) {
                System.out.println("Prescription Codes:");
                for (Prescription prescription : prescriptions) {
                    System.out.println(prescription.getPrescriptionCode());
                }
                ObservableList<Prescription> prescriptionList = FXCollections.observableArrayList(prescriptions);
                prescodetable.setItems(prescriptionList);
            } else {
                showErrorAlert("No prescription codes found for the patient: " + patientName);
            }
        } else {
            showErrorAlert("Please enter a patient name.");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        presCodeColumn.setCellValueFactory(new PropertyValueFactory<>("prescriptionCode"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("generatedDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("generatedTime"));

        // Add a listener to handle row selection
        prescodetable.setRowFactory(tv -> {
            TableRow<Prescription> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Prescription rowData = row.getItem();
                    String prescriptionCode = rowData.getPrescriptionCode();
                    openPrescriptionScene(prescriptionCode);
                }
            });
            return row;
        });
    }

    private void openPrescriptionScene(String prescriptionCode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IndividualPrescription.fxml"));
            Parent root = loader.load();

            IndividualPrescription controller = loader.getController();
            controller.setPrescriptionCode(prescriptionCode);

            Scene scene = new Scene(root);
            Stage stage = (Stage) prescodetable.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
