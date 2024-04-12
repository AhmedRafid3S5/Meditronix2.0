package com.example.meditronix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreatePrescriptionController {

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField Age;

    @FXML
    private TextField Dosage;

    @FXML
    private TextField Frequency;

    @FXML
    private TextField Gender;

    @FXML
    private TextField MedicineName;

    @FXML
    private TextField Name;

    @FXML
    private TextField Quantity;

    @FXML
    private Button addMedicineButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField medicineDosage;

    @FXML
    private VBox medicineEntryTemplate;

    @FXML
    private TextField medicineFrequency;

    @FXML
    private TextField medicineName;

    @FXML
    private TextField medicineQuantity;

    @FXML
    private Button removeMedicineButton;

    @FXML
    private Button stopPrescriptionButton;

    @FXML
    private Label MedicineCountLabel;

    private List<Medicine> medicines = new ArrayList<>();

    private int medicineCount = 0;
    private static final int MAX_MEDICINES = 10;

    @FXML
    void addMedicine(ActionEvent event) {
        if (medicineCount < MAX_MEDICINES) {
            String name = medicineName.getText();
            String dosage = medicineDosage.getText();
            String quantity = medicineQuantity.getText();
            String frequency = medicineFrequency.getText();

            if (validateMedicineFields(name, dosage, quantity, frequency)) {
                Medicine medicine = new Medicine(name, dosage, quantity, frequency);
                medicines.add(medicine);
                medicineCount++;

                // Update the label to display the current medicine count
                MedicineCountLabel.setText("Medicine Count: " + medicineCount);

                System.out.println("Added medicine: " + medicine);
                medicineName.clear();
                medicineDosage.clear();
                medicineQuantity.clear();
                medicineFrequency.clear();
            } else {
                showErrorAlert("Please fill in all medicine details.");
            }
        } else {
            showErrorAlert("Maximum number of medicines reached.");
        }
    }

    private boolean validateMedicineFields(String name, String dosage, String quantity, String frequency) {
        return !name.isEmpty() && !dosage.isEmpty() && !quantity.isEmpty() && !frequency.isEmpty();
    }

    private void showErrorAlert(String message) {
        // You can implement this method to display an error alert to the user
        System.out.println("Error: " + message);
    }

    @FXML
    void ageTfPressed(ActionEvent event) {
        String ageText = Age.getText();
        System.out.println("Age: " + ageText);
    }

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
    void dosageTfPressed(ActionEvent event) {
        String dosageText = Dosage.getText();
        System.out.println("Dosage: " + dosageText);
    }

    @FXML
    void frequencyTfPressed(ActionEvent event) {
        String frequencyText = Frequency.getText();
        System.out.println("Frequency: " + frequencyText);
    }

    @FXML
    void genderTfPressed(ActionEvent event) {
        String genderText = Gender.getText();
        System.out.println("Gender: " + genderText);
    }

    @FXML
    void medicineNameTfPressed(ActionEvent event) {
        String medicineNameText = MedicineName.getText();
        System.out.println("Medicine Name: " + medicineNameText);
    }

    @FXML
    void nameTfPressed(ActionEvent event) {
        String nameText = Name.getText();
        System.out.println("Name: " + nameText);
    }

    @FXML
    void quantityTfPressed(ActionEvent event) {
        String quantityText = Quantity.getText();
        System.out.println("Quantity: " + quantityText);
    }

    @FXML
    void removeMedicine(ActionEvent event) {
        if (!medicines.isEmpty()) {
            Medicine removedMedicine = medicines.remove(medicines.size() - 1);
            System.out.println("Removed medicine: " + removedMedicine);
            medicineCount--;
            MedicineCountLabel.setText("Medicine Count: " + medicineCount);
        } else {
            System.out.println("No medicines to remove.");
        }
    }

    @FXML
    void stopPrescription(ActionEvent event) {
        System.out.println("Prescription stopped.");
        medicines.clear();
        medicineCount = 0;
        MedicineCountLabel.setText("Medicine Count: " + medicineCount);
    }

    private static class Medicine {
        private final String name;
        private final String dosage;
        private final String quantity;
        private final String frequency;

        public Medicine(String name, String dosage, String quantity, String frequency) {
            this.name = name;
            this.dosage = dosage;
            this.quantity = quantity;
            this.frequency = frequency;
        }

        @Override
        public String toString() {
            return "Medicine{" +
                    "name='" + name + '\'' +
                    ", dosage='" + dosage + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", frequency='" + frequency + '\'' +
                    '}';
        }
    }
}
